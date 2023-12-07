package com.kospin.myapplication.model.mainmodel.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kospin.myapplication.adapter.SuratAdapter
import com.kospin.myapplication.roomdb.DbArsipSurat
import com.kospin.myapplication.databinding.FragmentAllSuratBinding
import com.kospin.myapplication.utils.DatePicker
import com.kospin.myapplication.viewmodel.SuratRepository
import com.kospin.myapplication.viewmodel.SuratViewModel
import com.kospin.myapplication.viewmodel.SuratViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllSuratFragment : Fragment() {

    private var _find: FragmentAllSuratBinding? = null
    private val find get() = _find!!
    private lateinit var adapter: SuratAdapter
    private lateinit var selectedSpn: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentAllSuratBinding.inflate(inflater, container, false)
        adapter = SuratAdapter(arrayListOf(), object : SuratAdapter.Onclik{
            override fun deleteSurat(id: Int) {
                deleteData(id)
            }
        })
        find.rvArsipSurat.adapter = adapter
        find.rvArsipSurat.layoutManager = LinearLayoutManager(requireContext())
        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
        val username = sheredPreferences.getString("username", null)
        find.tvUsername.setText(username.toString())

        find.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(key: CharSequence?, start: Int, before: Int, count: Int) {
                if (key.isNullOrEmpty()){
                    tampilData()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = viewModel().cariSurat("%$key%")
                        adapter.setData(data)
                        withContext(Dispatchers.Main){
                            adapter.notifyDataSetChanged()
                            if (data.isEmpty()){
                                find.tvNotifikasi.visibility = View.VISIBLE
                            } else {
                                find.tvNotifikasi.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        })

        find.tvFilterTanggal.setOnClickListener {
            val datePicker  = DatePicker()
            datePicker.setOnDateSetListener { selectedDate ->
                find.tvFilterTanggal.setText(selectedDate)
                find.btnFltTanggalCancel.visibility = View.VISIBLE
            }
            datePicker.show(requireFragmentManager(), "datePicker")
        }

        find.btnFltTanggalCancel.setOnClickListener {
            find.tvFilterTanggal.text = null
            find.btnFltTanggalCancel.visibility = View.GONE
        }

        find.tvFilterTanggal.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    selectedSpn == "unit/divisi" && find.tvFilterTanggal.text.isEmpty() -> {
                        tampilData()
                    }
                    selectedSpn == "unit/divisi" && find.tvFilterTanggal.text.isNotEmpty() -> {
                        setByTanggal()
                    }
                    selectedSpn != "unit/divisi" && find.tvFilterTanggal.text.isNotEmpty() -> {
                        setFiltered()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })

        val dataDivisi = viewModel().divisi
        val spnFilterDivisi = find.spnFilterDivisi
        val spnAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, dataDivisi)
        spnFilterDivisi.adapter = spnAdapter

        spnFilterDivisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSpn = parent?.getItemAtPosition(position).toString()
                find.etSearch.text.clear()
                when {
                    position == 0 && find.tvFilterTanggal.text.isEmpty() -> {
                        tampilData()
                    }
                    position != 0 && find.tvFilterTanggal.text.isEmpty() -> {
                        setByDivisi()
                    }
                    position != 0 && find.tvFilterTanggal.text.isNotEmpty() -> {
                        setFiltered()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

    }

    override fun onPause() {
        super.onPause()
        find.etSearch.text.clear()
        find.tvFilterTanggal.text = null
        find.spnFilterDivisi.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }

    override fun onResume() {
        super.onResume()
        tampilData()
    }

    private fun deleteData(id: Int) {

        val data = viewModel().getById(id)
        val builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle("hapus data")
        builder.setMessage("apakah anda yakin ingin menghapus surat ${data.hal}")

        builder.setPositiveButton("OK") { dialog, _ ->
            // Tindakan yang akan diambil saat tombol OK ditekan (bisa kosong jika tidak diperlukan)
            viewModel().deleteSrt(data)
            dialog.dismiss()
            tampilData()
            alert("surat berhasil dihapus")
        }

        builder.setNegativeButton("batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun alert(msg: String) {
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun tampilData(){
        val data = viewModel().getAllSurat()
        adapter.setData(data)
        adapter.notifyDataSetChanged()
        find.tvNotifikasi.visibility = View.GONE
    }

    private fun viewModel(): SuratViewModel {
        val db by lazy { DbArsipSurat.getInstance(this.requireContext()) }
        val repository = SuratRepository(db)
        val factory = SuratViewModelFactory(repository)
        return ViewModelProviders.of(this, factory).get(SuratViewModel::class.java)
    }

    private fun setByTanggal() {
        val data = viewModel().getByTanggal(find.tvFilterTanggal.text.toString())
        adapter.setData(data)
        adapter.notifyDataSetChanged()
        if (data.isEmpty()){
            find.tvNotifikasi.visibility = View.VISIBLE
        } else {
            find.tvNotifikasi.visibility = View.GONE
        }
    }

    private fun setFiltered() {
        val data = viewModel().getFiltered(selectedSpn, find.tvFilterTanggal.text.toString())
        adapter.setData(data)
        adapter.notifyDataSetChanged()
        if (data.isEmpty()){
            find.tvNotifikasi.visibility = View.VISIBLE
        } else {
            find.tvNotifikasi.visibility = View.GONE
        }
    }

    private fun setByDivisi() {
        val data = viewModel().getByDivisi(selectedSpn)
        adapter.setData(data)
        adapter.notifyDataSetChanged()
        if (data.isEmpty()){
            find.tvNotifikasi.visibility = View.VISIBLE
        } else {
            find.tvNotifikasi.visibility = View.GONE
        }
    }

}