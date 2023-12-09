package com.kospin.myapplication.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kospin.myapplication.adapter.DataAdapterSurat

@Dao
interface DAO {
    @Query("SELECT * FROM tb_surat")
    fun getAllSrt() : List<Surat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat ORDER BY id DESC")
    fun getAllSrtNoFoto() : LiveData<List<DataAdapterSurat>>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE jenis = :jenis ORDER BY id DESC")
    fun getJenisSrtNoFoto(jenis: String) : LiveData<List<DataAdapterSurat>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSrt(surat: Surat)
    @Update
    fun updateSrt(surat: Surat)
    @Delete
    fun deleteSrt(surat: Surat)
    @Query("SELECT * FROM tb_surat WHERE id = :id")
    fun getById(id: Int) : Surat
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE no_surat LIKE :key OR hal LIKE :key")
    fun cariSurat(key: String) : List<DataAdapterSurat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE divisi = :divisi")
    fun getByDivisi(divisi: String) : List<DataAdapterSurat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE tanggal = :tanggal")
    fun getByTanggal(tanggal: String) : List<DataAdapterSurat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE divisi = :divisi AND tanggal = :tanggal")
    fun getFiltered(divisi: String, tanggal: String) : List<DataAdapterSurat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat WHERE (no_surat LIKE :key OR hal LIKE :key) AND jenis = :jenis")
    fun cariSuratWithJenis(key: String, jenis: String) : List<DataAdapterSurat>
}