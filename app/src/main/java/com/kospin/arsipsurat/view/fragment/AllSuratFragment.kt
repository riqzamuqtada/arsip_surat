package com.kospin.arsipsurat.view.fragment

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
import com.kospin.arsipsurat.databinding.FragmentAllSuratBinding
import com.kospin.arsipsurat.utils.DatePicker
import com.kospin.arsipsurat.utils.PublicFunction
import com.kospin.arsipsurat.viewmodel.SuratViewModel

class AllSuratFragment : Fragment() {

    private var _find: FragmentAllSuratBinding? = null
    private val find get() = _find!!
    private lateinit var adapter: SuratAdapter
    private lateinit var selectedSpn: String
    private lateinit var selectedTgl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentAllSuratBinding.inflate(inflater, container, false)
        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        set username
        viewModel().username.observe(viewLifecycleOwner, Observer {
            find.tvUsername.setText(it.toString())
        })

//        adapter
        adapter = SuratAdapter(arrayListOf(), viewModel())
        find.rvArsipSurat.adapter = adapter
        find.rvArsipSurat.layoutManager = LinearLayoutManager(requireContext())

//        search data
        find.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(key: CharSequence?, start: Int, before: Int, count: Int) {
                if (key.isNullOrEmpty()){
                    tampilData()
                    find.btnFltSearchCancel.visibility = View.GONE
                    find.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
                } else {
                    setSearch(key)
                    find.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                    find.btnFltSearchCancel.visibility = View.VISIBLE
                }
            }
        })

        find.btnFltSearchCancel.setOnClickListener {
            find.etSearch.text.clear()
            find.etSearch.clearFocus()
            PublicFunction.hideKeyboard(requireContext(), find.etSearch)
            find.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
            find.btnFltSearchCancel.visibility = View.GONE
        }

//        filter
//          tanggal
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
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedTgl = find.tvFilterTanggal.text.toString()
                filter()
            }

        })

//          divisi
        val spnFilterDivisi = find.spnFilterDivisi
        val spnAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel().divisi)
        spnFilterDivisi.adapter = spnAdapter

        spnFilterDivisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSpn = parent?.getItemAtPosition(position).toString()
                selectedTgl = find.tvFilterTanggal.text.toString()
                filter()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

    }

    override fun onPause() {
        super.onPause()
        find.etSearch.text.clear()
        find.tvFilterTanggal.text = null
        find.btnFltTanggalCancel.visibility = View.GONE
        find.spnFilterDivisi.setSelection(0)
    }

    override fun onResume() {
        super.onResume()
        tampilData()
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
            find.lyNotifikasiSrt.visibility = View.VISIBLE
            if (type) {
                find.imgNotif.setImageResource(R.drawable.img_empty_data)
                find.tvNotifLabel.setText(R.string.empty_data_label)
                find.tvNotifDesc.setText(R.string.empty_data_desc)
            } else {
                find.imgNotif.setImageResource(R.drawable.img_not_found)
                find.tvNotifLabel.setText(R.string.notfound_data_label)
                find.tvNotifDesc.setText(R.string.notfound_data_desc)
            }
        } else {
            find.lyNotifikasiSrt.visibility = View.GONE
        }
    }

    private fun tampilData(){
        val data = viewModel().getAllSurat
        data.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            showNotif(it, true)
        })
    }

    private fun setSearch(key: CharSequence) {
        val data = viewModel().cariSurat("%$key%")
        adapter.setData(data)
        showNotif(data)
    }

    private fun setByTanggal() {
        val data = viewModel().getByTanggal(selectedTgl)
        adapter.setData(data)
        showNotif(data)
    }

    private fun setByDivisi() {
        val data = viewModel().getByDivisi(selectedSpn)
        adapter.setData(data)
        showNotif(data)
    }

    private fun setFiltered() {
        val data = viewModel().getFiltered(selectedSpn, selectedTgl)
        adapter.setData(data)
        showNotif(data)
    }

}