package com.kospin.arsipsurat.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieEntry
import com.kospin.arsipsurat.roomdb.Surat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuratViewModel(private val repository: SuratRepository) : ViewModel() {

    private val coroutine = CoroutineScope(Dispatchers.Main)
    private val masuk = "Masuk"
    private val keluar = "Keluar"

//    data divisi
    val divisi: Array<String> = arrayOf("unit/divisi", "Pengurus", "Kesekretariatan", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional")

//    edit data surat
    fun insertSrt(surat: Surat) = coroutine.launch { repository.insertSrt(surat) }
    fun updateSrt(surat: Surat) = coroutine.launch { repository.updateSrt(surat) }
    fun deleteSrt(surat: Surat) = coroutine.launch { repository.deleteSrt(surat) }

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

//    get jumlah data
    private fun getJumlahByJenis(jenis: String) = repository.getJumlahByJenis(jenis)
    fun getJumlahDivisiByJenis(divisi: String, jenis: String) = repository.getJumlahDivisiByJenis(divisi, jenis)

//    jumlah data jenis(masuk/keluar)
    val jumlahMasuk = getJumlahByJenis(masuk)
    val jumlahKeluar = getJumlahByJenis(keluar)
    val dataPieJenis = MediatorLiveData<List<PieEntry>>().apply {
        addSource(jumlahMasuk) { combineDataJenis() }
        addSource(jumlahKeluar) { combineDataJenis() }
    }
    private fun combineDataJenis() {
        val newData = listOf(
            PieEntry(jumlahMasuk.value ?: 0f, "Surat Masuk"),
            PieEntry(jumlahKeluar.value ?: 0f, "Surat Keluar")
        )
        dataPieJenis.value = newData
    }

//    set data pieChart masuk
    private val jumlahDataDivisiMasuk = divisi.drop(1).map {
        getJumlahDivisiByJenis(divisi[divisi.indexOf(it)], masuk)
    }
    val dataPieMasuk = MediatorLiveData<List<PieEntry>>().apply {
        jumlahDataDivisiMasuk.forEach { liveData ->
            addSource(liveData) { combinedDataDivisiJenis(true) }
        }
    }

//    set data pieChart keluar
    private val jumlahDataDivisiKeluar = divisi.drop(1).map {
        getJumlahDivisiByJenis(divisi[divisi.indexOf(it)], keluar)
    }
    val dataPieKeluar = MediatorLiveData<List<PieEntry>>().apply {
        jumlahDataDivisiKeluar.forEach { liveData ->
            addSource(liveData) { combinedDataDivisiJenis(false) }
        }
    }

    private fun combinedDataDivisiJenis(type: Boolean) {
        if (type) {
            val newData = jumlahDataDivisiMasuk.mapIndexed { index, liveData ->
                PieEntry(liveData.value ?: 0f, divisi[index+1])
            }
            dataPieMasuk.value = newData
        } else {
            val newData = jumlahDataDivisiKeluar.mapIndexed { index, liveData ->
                PieEntry(liveData.value ?: 0f, divisi[index+1])
            }
            dataPieKeluar.value = newData
        }
    }

}