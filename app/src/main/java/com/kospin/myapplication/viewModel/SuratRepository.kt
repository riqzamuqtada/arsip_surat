package com.kospin.myapplication.viewmodel

import com.kospin.myapplication.roomdb.DbArsipSurat
import com.kospin.myapplication.roomdb.Surat

class SuratRepository(private val db: DbArsipSurat) {

    fun deleteSrt(surat: Surat) = db.dao().deleteSrt(surat)
    fun getAllSurat() = db.dao().getSrtNoFoto()
    fun cariSurat(key: String) = db.dao().cariSurat(key)
    fun getByTanggal(tanggal: String) = db.dao().getByTanggal(tanggal)
    fun getFiltered(divisi: String, tanggal: String) = db.dao().getFiltered(divisi, tanggal)
    fun getByDivisi(divisi: String) = db.dao().getByDivisi(divisi)
    fun getById(id: Int) = db.dao().getById(id)

}