package com.kospin.arsipsurat.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Surat::class], version = 4)
abstract class DbArsipSurat : RoomDatabase(){
    abstract fun dao() : DAO
    companion object{
        @Volatile
        private var instance : DbArsipSurat? = null

        @Synchronized
        fun getInstance(context: Context): DbArsipSurat {
            if (instance == null) {
                instance = Room.databaseBuilder(context, DbArsipSurat::class.java, "db_arsip_surat")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}
