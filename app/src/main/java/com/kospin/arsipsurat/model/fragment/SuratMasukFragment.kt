package com.kospin.arsipsurat.model.fragment

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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.adapter.DataAdapterSurat
import com.kospin.arsipsurat.adapter.SuratAdapter
import com.kospin.arsipsurat.databinding.FragmentSuratMasukBinding
import com.kospin.arsipsurat.utils.DatePicker
import com.kospin.arsipsurat.utils.PublicFunction
import com.kospin.arsipsurat.viewmodel.SuratViewModel

class SuratMasukFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentSuratMasukBinding? = null
    private val find get() = _find!!
    private lateinit var adapter: SuratAdapter
    private lateinit var selectedSpn: String
    private lateinit var selectedTgl: String
    private val jenis: String = "Masuk"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentSuratMasukBinding.inflate(inflater, container, false)
        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        set username
        val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
        val username = sheredPreferences.getString("username", null)
        find.tvUsername.setText(username.toString())

//        set adapter
        adapter = SuratAdapter(arrayListOf(), viewModel())
        find.rvArsipSuratMasuk.adapter = adapter
        find.rvArsipSuratMasuk.layoutManager = LinearLayoutManager(requireContext())

//        search
        find.etSearchMasuk.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(key: CharSequence?, start: Int, before: Int, count: Int) {
                if (key.isNullOrEmpty()) {
                    tampilData()
                } else {
                    setSearch(key)
                }
            }
        })

//        filter
//        tanggal
        find.tvFilterTanggalMasuk.setOnClickListener {
            val datePicker = DatePicker()
            datePicker.setOnDateSetListener {
                find.tvFilterTanggalMasuk.setText(it)
                find.btnCancelTanggalMasuk.visibility = View.VISIBLE
            }
            datePicker.show(requireFragmentManager(), "datePicker")
        }

        find.btnCancelTanggalMasuk.setOnClickListener {
            find.tvFilterTanggalMasuk.text = null
            find.btnCancelTanggalMasuk.visibility = View.GONE
        }

        find.tvFilterTanggalMasuk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedTgl = find.tvFilterTanggalMasuk.text.toString()
                filter()
            }
        })

//          divisi
        val spnFilterDivisi = find.spnFilterDivisiMasuk
        val spnAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel().divisi)
        spnFilterDivisi.adapter = spnAdapter

        spnFilterDivisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSpn = parent?.getItemAtPosition(position).toString()
                selectedTgl = find.tvFilterTanggalMasuk.text.toString()
                filter()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        tampilData()
    }

    override fun onPause() {
        super.onPause()
        find.etSearchMasuk.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }

    private fun viewModel(): SuratViewModel {
        return PublicFunction.getSuratViewModel(requireContext())
    }

    private fun filter() {
        when {
            selectedSpn == viewModel().divisi[0] && selectedTgl.isEmpty() -> {
                tampilData()
            }
            selectedSpn != viewModel().divisi[0] && selectedTgl.isEmpty() -> {
                setByDivisi()
            }
            selectedSpn == viewModel().divisi[0] && selectedTgl.isNotEmpty() -> {
                setByTanggal()
            }
            selectedSpn != viewModel().divisi[0] && selectedTgl.isNotEmpty() -> {
                setFiltered()
            }
        }
    }

    private fun showNotif(data: List<DataAdapterSurat>, type: Boolean = false) {
        if (data.isEmpty()){
            find.lyNotifikasiSrtMasuk.visibility = View.VISIBLE
            if (type) {
                find.imgNotifMasuk.setImageResource(R.drawable.img_empty_data)
                find.tvNotifLabelMasuk.setText(R.string.empty_data_label)
                find.tvNotifDescMasuk.setText(R.string.empty_data_desc)
            } else {
                find.imgNotifMasuk.setImageResource(R.drawable.img_not_found)
                find.tvNotifLabelMasuk.setText(R.string.notfound_data_label)
                find.tvNotifDescMasuk.setText(R.string.notfound_data_desc)
            }
        } else {
            find.lyNotifikasiSrtMasuk.visibility = View.GONE
        }
    }

    private fun tampilData() {
        val data = viewModel().getJenisSrtNoFoto(jenis)
        data.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            showNotif(it, true)
        })
    }

    private fun setSearch(key: CharSequence) {
        val data = viewModel().cariSuratWithJenis("%$key%", jenis)
        adapter.setData(data)
        showNotif(data)
    }

    private fun setByDivisi() {
        val data = viewModel().getByDivisiWithJenis(selectedSpn, jenis)
        adapter.setData(data)
        showNotif(data)
    }

    private fun setByTanggal() {
        val data = viewModel().getByTanggalWithJenis(selectedTgl, jenis)
        adapter.setData(data)
        showNotif(data)
    }

    private fun setFiltered() {
        val data = viewModel().getFilteredWithJenis(selectedSpn, selectedTgl, jenis)
        adapter.setData(data)
        showNotif(data)
    }

}