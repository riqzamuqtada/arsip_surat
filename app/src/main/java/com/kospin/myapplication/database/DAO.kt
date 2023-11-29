package com.kospin.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Query("SELECT * FROM tb_surat")
    fun getAllSrt() : List<Surat>
    @Query("SELECT id, no_surat, hal, jenis, divisi, tanggal FROM tb_surat ORDER BY id DESC")
    fun getSrtNoFoto() : List<DataAdapterSurat>
    @Insert
    fun insertSrt(surat: Surat)
    @Update
    fun updateSrt(surat: Surat)
    @Delete
    fun deleteSrt(surat: Surat)
    @Query("SELECT * FROM tb_surat WHERE id = :id")
    fun getById(id: Int) : List<Surat>
}