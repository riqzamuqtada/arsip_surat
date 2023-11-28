package com.kospin.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.github.chrisbanes.photoview.BuildConfig
import com.google.android.material.datepicker.MaterialDatePicker
import com.kospin.myapplication.database.DbArsipSurat
import com.kospin.myapplication.database.Surat
import com.kospin.myapplication.databinding.ActivityInputBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InputActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputBinding
    private lateinit var selectedItemDivisi : String
    private lateinit var selectedItemJenis : String
    private lateinit var foto : ByteArray
    private var currentPhotoPath: String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var opsiDivisi : String = "0"
    private var opsiJenis : String = "0"
    private val db by lazy { DbArsipSurat.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityInputBinding.inflate(layoutInflater)
        setContentView(find.root)

        val dataDivisi = arrayOf("unit/divisi", "Pengurus", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional", "Kesekretariatan")
        val spnDivisi = find.spInputDivisi
        setupSpinner(spnDivisi, dataDivisi, opsiDivisi)
        spnDivisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItemDivisi = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        val dataJenis = arrayOf("pilih jenis", "Masuk", "Keluar")
        val spnJenis = find.spInputJenis
        setupSpinner(spnJenis, dataJenis,  opsiJenis)
        spnJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItemJenis = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        find.tvInputTanggal.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()

            picker.addOnPositiveButtonClickListener { selection ->
                val dateFormat = SimpleDateFormat("dd/MMMM/yyyy", Locale("id", "ID"))
                val selectedDate = dateFormat.format(selection)
                val date = selectedDate.replace("/", " ")

                find.tvInputTanggal.setText(date)
            }

            picker.show(supportFragmentManager, picker.toString())
        }
        find.tvInputFoto.setOnClickListener {
            showOptionsDialog()
        }

        find.btnInputInsert.setOnClickListener {
            if (
                find.etInputNomor.text.isNotEmpty() &&
                find.etInputHal.text.isNotEmpty() &&
                find.tvInputTanggal.text.isNotEmpty() &&
                selectedItemJenis !== "pilih jenis" &&
                selectedItemDivisi !== "unit/divisi" &&
                foto.isNotEmpty()
            ){
                try {
                    insertSurat()
                } catch (e:Exception){
                    alert("Error : $e")
                }
            } else {
                alert("Wajib di isi")
            }
        }

    }

    private fun insertSurat() {
        CoroutineScope(Dispatchers.IO).launch {
            db.dao().insertSrt(Surat(
                0,
                find.etInputNomor.text.toString(),
                find.etInputHal.text.toString(),
                selectedItemJenis,
                selectedItemDivisi,
                find.tvInputTanggal.text.toString(),
                find.etInputCatatan.text.toString(),
                foto
            ))
            withContext(Dispatchers.Main){
                alert("Data berhasil ditambahkan")
                onBackPressed()
            }
        }
    }

    private fun alert(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupSpinner(spinner: Spinner, data: Array<String>, opsi: String){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(opsi.toInt())
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Kamera", "Galeri")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih salah satu")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> dispatchPickImageIntent()
                }
            }
            .show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.fileprovider1",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchPickImageIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    // Gambar telah diambil dari kamera, currentPhotoPath berisi path ke file gambar
                    // Lakukan sesuatu dengan currentPhotoPath
                    val imgCompres = compressImageToByteArray(currentPhotoPath!!, this)
                    foto = imgCompres

                    find.tvInputFoto.text = File(currentPhotoPath).name
                }
                REQUEST_IMAGE_PICK -> {
                    // Gambar telah dipilih dari galeri
                    val selectedImage = getPathFromUri(data?.data!!)
                    // Lakukan sesuatu dengan selectedImage
                    val imgCompres = compressImageToByteArray(selectedImage, this)
                    foto = imgCompres

                    find.tvInputFoto.text = File(selectedImage).name
                }
            }
        }

    }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }

        return ""
    }

    // Fungsi untuk mengkompres gambar menjadi ByteArray dengan mempertahankan orientasi
    private fun compressImageToByteArray(imagePath: String, context: Context): ByteArray {
        // Membaca orientasi dari metadata Exif
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // Konfigurasi untuk kompresi gambar
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)

        // Hitung skala kompresi berdasarkan ukuran yang diinginkan
        val scale = calculateScale(options, targetSizeInMb = 1.0)

        // Konfigurasi ulang untuk mendekompresi gambar dengan skala yang benar
        options.inJustDecodeBounds = false
        options.inSampleSize = scale

        // Baca gambar ke dalam Bitmap
        val compressedBitmap = BitmapFactory.decodeFile(imagePath, options)

        // Memperbarui orientasi gambar
        val rotatedBitmap = rotateBitmap(compressedBitmap, orientation)

        // Konversi Bitmap ke ByteArray
        val outputStream = ByteArrayOutputStream()
        rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Ganti format dan kualitas sesuai kebutuhan
        return outputStream.toByteArray()
    }

    // Fungsi untuk menghitung skala kompresi berdasarkan ukuran target dalam megabyte
    private fun calculateScale(options: BitmapFactory.Options, targetSizeInMb: Double): Int {
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        val imageSizeInMb = imageHeight * imageWidth / (1024.0 * 1024.0)

        var scale = 1
        while (imageSizeInMb / (scale * scale) > targetSizeInMb) {
            scale *= 2
        }

        return scale
    }

    private fun rotateBitmap(bitmap: Bitmap?, orientation: Int): Bitmap? {
        val matrix = android.graphics.Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }
        return Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}
