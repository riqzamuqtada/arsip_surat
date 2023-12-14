package com.kospin.myapplication.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.kospin.myapplication.roomdb.DbArsipSurat
import com.kospin.myapplication.viewmodel.SuratRepository
import com.kospin.myapplication.viewmodel.SuratViewModel
import com.kospin.myapplication.viewmodel.SuratViewModelFactory

object PublicFunction {

    fun getSuratViewModel(context: Context): SuratViewModel {
        val db = DbArsipSurat.getInstance(context)
        val repository = SuratRepository(db)
        val factory = SuratViewModelFactory(repository)
        return ViewModelProviders.of(context as androidx.fragment.app.FragmentActivity, factory)
            .get(SuratViewModel::class.java)
    }

    fun alert(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}