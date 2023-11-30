package com.kospin.myapplication

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kospin.myapplication.database.DbArsipSurat
import com.kospin.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var find: ActivityDetailBinding
    private val db by lazy { DbArsipSurat.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(find.root)

        val id = intent.getIntExtra("id", -1)
        val data = db.dao().getById(id)[0]
        val bitmap = BitmapFactory.decodeByteArray(data.gambar, 0, data.gambar.size)

//        set data
        find.imgDetailSurat.setImageBitmap(bitmap)
        find.tvDetailNomor.setText(data.no_surat)
        find.tvDetailHal.setText(data.hal)
        find.tvDetailJenis.setText(data.jenis)
        find.tvDetailDivisi.setText(data.divisi)
        find.tvDetailTanggal.setText(data.tanggal)
        find.tvDetailCatatan.setText(data.catatan)
        find.imgPhotoView.setImageBitmap(bitmap)
        find.tvZoomHal.setText(data.hal)

//        fungsi button
        find.btnDetailBack.setOnClickListener {
            onBackPressed()
        }
        find.imgDetailSurat.setOnClickListener {
            find.lyFotoZoom.visibility = View.VISIBLE
        }
        find.btnZoomBack.setOnClickListener {
            find.lyFotoZoom.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        if (find.lyFotoZoom.visibility == View.VISIBLE){
            find.lyFotoZoom.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}