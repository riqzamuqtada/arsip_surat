package com.kospin.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kospin.myapplication.adapter.SuratAdapter
import com.kospin.myapplication.database.DataAdapterSurat
import com.kospin.myapplication.database.DbArsipSurat
import com.kospin.myapplication.databinding.AdapterSuratBinding
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
    private lateinit var find: FragmentAllSuratBinding
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
        find = FragmentAllSuratBinding.inflate(layoutInflater)

        viewModel.username.observe(viewLifecycleOwner, Observer { username ->
            find.tvUsername.setText(username)
        })

        adapter = SuratAdapter(arrayListOf(), object : SuratAdapter.Onclik{
            override fun deleteSurat(id: Int) {
                deleteData(id)
            }


        })

        return find.root
    }

    private fun deleteData(id: Int) {

        val data = db.dao().getById(id)[0]

        val builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle("hapus data")
        builder.setMessage("apakah anda yakin ingin menghapus surat ${data.hal}")

        builder.setPositiveButton("OK") { dialog, _ ->
            // Tindakan yang akan diambil saat tombol OK ditekan (bisa kosong jika tidak diperlukan)
            CoroutineScope(Dispatchers.IO).launch {
                db.dao().deleteSrt(data)
                withContext(Dispatchers.Main){
                    dialog.dismiss()
                    tampilData()
                    alert("surat berhasil dihapus")
                }
            }
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