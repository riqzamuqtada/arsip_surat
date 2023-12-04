package com.kospin.myapplication.fragment

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kospin.myapplication.R
import com.kospin.myapplication.adapter.SuratAdapter
import com.kospin.myapplication.database.DbArsipSurat
import com.kospin.myapplication.databinding.FragmentAllSuratBinding
import com.kospin.myapplication.viewModel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllSuratFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllSuratFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentAllSuratBinding? = null
    private val find get() = _find!!
    private lateinit var adapter: SuratAdapter
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: MyViewModel by activityViewModels()
    private val db by lazy { DbArsipSurat.getInstance(this.requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentAllSuratBinding.inflate(inflater, container, false)
        if (isResumed) find.etSearch.text.clear()
        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SuratAdapter(arrayListOf(), object : SuratAdapter.Onclik{
            override fun deleteSurat(id: Int) {
                deleteData(id)
            }
        })

        viewModel.username.observe(viewLifecycleOwner, Observer { username ->
            find.tvUsername.setText(username)
        })

        find.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(key: CharSequence?, start: Int, before: Int, count: Int) {
                if (key.isNullOrEmpty()){
                    tampilData()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = db.dao().cariSurat("%$key%")
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

        val dataDivisi = arrayOf("unit/divisi", "Pengurus", "Div. Pinjaman", "Div. Dana", "Div. Pengawasan", "Div. Operasional", "Kesekretariatan")
        val spnFilterDivisi = find.spnFilterDivisi
        val spnAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, dataDivisi)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnFilterDivisi.adapter = spnAdapter

    }

    override fun onPause() {
        super.onPause()
        find.etSearch.text.clear()
        find.spnFilterDivisi.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }

    private fun deleteData(id: Int) {

        val data = db.dao().getById(id)[0]
        val builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle("Hapus Arsip Surat")
        builder.setMessage("Apakah anda yakin ingin menghapus\n\"${data.hal}\" ?")

        builder.setPositiveButton("Hapus") { dialog, _ ->
            // Tindakan yang akan diambil saat tombol OK ditekan (bisa kosong jika tidak diperlukan)
            CoroutineScope(Dispatchers.IO).launch {
                db.dao().deleteSrt(data)
                withContext(Dispatchers.Main){
                    dialog.dismiss()
                    tampilData()
                    alert("Data Arsip Surat berhasil dihapus!")
                }
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        // Ambil tombol positif setelah dialog ditampilkan
        dialog.setOnShowListener {
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.clr_red))
        }

        dialog.show()
    }

    private fun alert(msg: String) {
        Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        tampilData()
    }

    private fun tampilData(){
        find.rvArsipSurat.layoutManager = LinearLayoutManager(this.requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val data = db.dao().getSrtNoFoto()
            adapter.setData(data)
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                find.tvNotifikasi.visibility = View.GONE
            }
        }
        find.rvArsipSurat.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllSuratFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllSuratFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}