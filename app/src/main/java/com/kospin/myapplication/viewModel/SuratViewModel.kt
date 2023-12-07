package com.kospin.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kospin.myapplication.roomdb.Surat

class SuratViewModel(private val repository: SuratRepository) : ViewModel() {


//    username
    private var _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username
    fun setUser(setUsername: String){
        _username.value = setUsername
    }

//    data divisi
    val divisi: Array<String> = arrayOf("unit/divisi", "Pengurus", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional", "Kesekretariatan")

    fun deleteSrt(surat: Surat) = repository.deleteSrt(surat)
    fun getAllSurat() = repository.getAllSurat()
    fun cariSurat(key: String) = repository.cariSurat(key)
    fun getByTanggal(tanggal: String) = repository.getByTanggal(tanggal)
    fun getFiltered(divisi: String, tanggal: String) = repository.getFiltered(divisi, tanggal)
    fun getByDivisi(divisi: String) = repository.getByDivisi(divisi)
    fun getById(id: Int) = repository.getById(id)

}