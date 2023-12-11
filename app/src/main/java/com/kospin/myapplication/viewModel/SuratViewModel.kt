package com.kospin.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kospin.myapplication.roomdb.Surat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuratViewModel(private val repository: SuratRepository) : ViewModel() {

    private val corouTine = CoroutineScope(Dispatchers.Main)

//    data divisi
    val divisi: Array<String> = arrayOf("unit/divisi", "Pengurus", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional", "Kesekretariatan")

//    edit data surat
    fun insertSrt(surat: Surat) = corouTine.launch { repository.insertSrt(surat) }
    fun updateSrt(surat: Surat) = corouTine.launch { repository.updateSrt(surat) }
    fun deleteSrt(surat: Surat) = corouTine.launch { repository.deleteSrt(surat) }

//    get data surat
    fun getAllSurat() = repository.getAllSurat()
    fun getJenisSrtNoFoto(jenis: String) = repository.getJenisSrtNoFoto(jenis)
    fun getById(id: Int) = repository.getById(id)
    fun cariSurat(key: String) = repository.cariSurat(key)
    fun getByTanggal(tanggal: String) = repository.getByTanggal(tanggal)
    fun getByDivisi(divisi: String) = repository.getByDivisi(divisi)
    fun getFiltered(divisi: String, tanggal: String) = repository.getFiltered(divisi, tanggal)
    fun cariSuratWithJenis(key: String, jenis: String) = repository.cariSuratWithJenis(key, jenis)
    fun getByDivisiWithJenis(divisi: String, jenis: String) = repository.getByDivisiWithJenis(divisi, jenis)
    fun getByTanggalWithJenis(tanggal: String, jenis: String) = repository.getByTanggalWithJenis(tanggal, jenis)
    fun getFilteredWithJenis(divisi: String, tanggal: String, jenis: String) = repository.getFilteredWithJenis(divisi, tanggal, jenis)

}