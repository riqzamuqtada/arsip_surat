package com.kospin.myapplication.viewmodel

import com.kospin.myapplication.roomdb.DbArsipSurat
import com.kospin.myapplication.roomdb.Surat

class SuratRepository(private val db: DbArsipSurat) {

    fun insertSrt(surat: Surat) = db.dao().insertSrt(surat)
    fun updateSrt(surat: Surat) = db.dao().updateSrt(surat)
    fun deleteSrt(surat: Surat) = db.dao().deleteSrt(surat)
    fun getAllSurat() = db.dao().getAllSrtNoFoto()
    fun getJenisSrtNoFoto(jenis: String) = db.dao().getJenisSrtNoFoto(jenis)
    fun getById(id: Int) = db.dao().getById(id)
    fun cariSurat(key: String) = db.dao().cariSurat(key)
    fun getByTanggal(tanggal: String) = db.dao().getByTanggal(tanggal)
    fun getByDivisi(divisi: String) = db.dao().getByDivisi(divisi)
    fun getFiltered(divisi: String, tanggal: String) = db.dao().getFiltered(divisi, tanggal)
    fun cariSuratWithJenis(key: String, jenis: String) = db.dao().cariSuratWithJenis(key, jenis)

}