package com.kospin.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kospin.myapplication.databinding.ActivityInputBinding

class InputActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputBinding

    private lateinit var selectedItemDivisi : String
    private lateinit var selectedItemJenis : String
    private var opsiDivisi : String = "0"
    private var opsiJenis : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityInputBinding.inflate(layoutInflater)
        setContentView(find.root)

        // set spinner divisi
        val dataDivisi = arrayOf("unit/divisi", "Pengurus", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional", "Kesekretariatan")
        val spnDivisi = find.spInputDivisi
        val spnDivisiAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataDivisi)
        spnDivisiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnDivisi.adapter = spnDivisiAdapter
        spnDivisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemDivisi = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
        spnDivisi.setSelection(opsiDivisi.toInt())

        // set spinner jenis
        val dataJenis = arrayOf("pilih jenis", "Masuk", "Keluar")
        val spnJenis = find.spInputJenis
        val spnJenisAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataJenis)
        spnJenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnJenis.adapter = spnJenisAdapter
        spnJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemJenis = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
        spnDivisi.setSelection(opsiJenis.toInt())

    }
}