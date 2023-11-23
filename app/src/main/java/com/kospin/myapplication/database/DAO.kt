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
    @Insert
    fun insertSrt(surat: Surat)
    @Update
    fun updateSrt(surat: Surat)
    @Delete
    fun deleteSrt(surat: Surat)
}