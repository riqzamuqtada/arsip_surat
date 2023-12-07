package com.kospin.myapplication.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var onDateSetListener: ((String) -> Unit)? = null // Listener untuk memberitahu pemanggil tanggal yang dipilih
    private var selectedDate: String = ""

    fun setOnDateSetListener(listener: (String) -> Unit) {
        onDateSetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Buat instance dari DatePickerDialog dan kembalikan
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val monthName = getMonthName(month)

        // Simpan tanggal yang dipilih
        selectedDate = "$dayOfMonth $monthName $year"

        // Panggil listener jika ada
        onDateSetListener?.invoke(selectedDate)
    }

    private fun getMonthName(month: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        val monthDate = calendar.time

        val sdf = SimpleDateFormat("MMMM", Locale("id", "ID"))
        return sdf.format(monthDate)
    }
}