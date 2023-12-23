package com.kospin.arsipsurat.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.kospin.arsipsurat.roomdb.DbArsipSurat
import com.kospin.arsipsurat.viewmodel.SuratRepository
import com.kospin.arsipsurat.viewmodel.SuratViewModel
import com.kospin.arsipsurat.viewmodel.SuratViewModelFactory

object PublicFunction {

    fun getSuratViewModel(context: Context): SuratViewModel {
        val db = DbArsipSurat.getInstance(context)
        val repository = SuratRepository(db)
        val factory = SuratViewModelFactory(repository)
        val viewModel = ViewModelProviders.of(context as androidx.fragment.app.FragmentActivity, factory).get(SuratViewModel::class.java)
        return viewModel
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun alert(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}