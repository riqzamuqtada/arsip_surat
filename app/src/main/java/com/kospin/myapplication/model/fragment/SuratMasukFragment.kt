package com.kospin.myapplication.model.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kospin.myapplication.adapter.DataAdapterSurat
import com.kospin.myapplication.adapter.SuratAdapter
import com.kospin.myapplication.databinding.FragmentSuratMasukBinding
import com.kospin.myapplication.utils.PublicFunction
import com.kospin.myapplication.viewmodel.SuratViewModel

class SuratMasukFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentSuratMasukBinding? = null
    private val find get() = _find!!
    private lateinit var adapter: SuratAdapter
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
        adapter = SuratAdapter(arrayListOf(), object : SuratAdapter.Onclik {
            override fun deleteSurat(id: Int) {
                deleteData(id)
            }
        })
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

    private fun showNotif(data: List<DataAdapterSurat>, text: String = "Data tidak ditemukan") {
        if (data.isEmpty()) {
            find.tvNotifikasiSrtMasuk.visibility = View.VISIBLE
            find.tvNotifikasiSrtMasuk.setText(text)
        } else {
            find.tvNotifikasiSrtMasuk.visibility = View.GONE
        }
    }

    private fun deleteData(id: Int) {

        val data = viewModel().getById(id)
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("hapus data")
        builder.setMessage("apakah anda yakin ingin menghapus surat ${data.hal}")

        builder.setPositiveButton("OK") { dialog, _ ->
            // Tindakan yang akan diambil saat tombol OK ditekan (bisa kosong jika tidak diperlukan)
            viewModel().deleteSrt(data)
            dialog.dismiss()
            tampilData()
            PublicFunction.alert("surat berhasil dihapus", requireContext())
        }

        builder.setNegativeButton("batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun tampilData() {
        val data = viewModel().getJenisSrtNoFoto(jenis)
        data.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            showNotif(it, "Tambah data terlebih dahulu")
        })
    }

    private fun setSearch(key: CharSequence) {
        val data = viewModel().cariSuratWithJenis("%$key%", jenis)
        adapter.setData(data)
        showNotif(data)
    }

}