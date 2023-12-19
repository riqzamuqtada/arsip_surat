package com.kospin.arsipsurat.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tb_surat")
data class Surat (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")       val id          : Int,
    @ColumnInfo("no_surat") val no_surat    : String,
    @ColumnInfo("hal")      val hal         : String,
    @ColumnInfo("jenis")    val jenis       : String,
    @ColumnInfo("divisi")   val divisi      : String,
    @ColumnInfo("tanggal")  val tanggal     : String,
    @ColumnInfo("catatan")  val catatan     : String,
    @ColumnInfo("gambar")   val gambar      : ByteArray
)