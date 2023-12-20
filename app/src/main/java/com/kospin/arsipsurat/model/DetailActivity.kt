package com.kospin.arsipsurat.model

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.roomdb.DbArsipSurat
import com.kospin.arsipsurat.databinding.ActivityDetailBinding
import com.kospin.arsipsurat.roomdb.Surat
import com.kospin.arsipsurat.utils.PublicFunction
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    private lateinit var find: ActivityDetailBinding
    private lateinit var initialScale: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(find.root)

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_blue_dark))

        val id = intent.getIntExtra("id", -1)
        val data: Surat? = PublicFunction.getSuratViewModel(this).getById(id)

        if (data != null) {

//        set data
            val bitmap = BitmapFactory.decodeByteArray(data.gambar, 0, data.gambar.size)
            find.imgDetailSurat.setImageBitmap(bitmap)
            find.tvDetailNomor.setText(data.no_surat)
            find.tvDetailHal.setText(data.hal)
            find.tvDetailJenis.setText(data.jenis)
            find.tvDetailDivisi.setText(data.divisi)
            find.tvDetailTanggal.setText(data.tanggal)
            find.tvDetailCatatan.setText(data.catatan)
            find.imgPhotoView.setImageBitmap(bitmap)
            find.tvZoomHal.setText(data.hal)
            initialScale = find.imgPhotoView.scale.toString()

        } else {
            PublicFunction.alert("Kesalahan atau data tidak ditemukan", this)
            finish()
        }

        val scaleUp     = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val scaleDown   = AnimationUtils.loadAnimation(this, R.anim.scale_down)

//        fungsi button
        find.btnDetailBack.setOnClickListener {
            onBackPressed()
        }
        find.imgDetailSurat.setOnClickListener {
            find.lyFotoZoom.startAnimation(scaleUp)
            find.lyFotoZoom.visibility = View.VISIBLE
        }
        find.btnZoomBack.setOnClickListener {
            find.lyFotoZoom.startAnimation(scaleDown)
            onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.from_down, R.anim.pinch_out)
    }

    override fun onBackPressed() {
        if (find.lyFotoZoom.visibility == View.VISIBLE){
            find.lyFotoZoom.visibility = View.GONE
            find.imgPhotoView.scale = initialScale.toFloat()
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.pinch_in, R.anim.to_down)
        }
    }
}