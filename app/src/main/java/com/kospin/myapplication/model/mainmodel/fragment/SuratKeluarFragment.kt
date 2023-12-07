package com.kospin.myapplication.model.mainmodel.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kospin.myapplication.databinding.FragmentSuratKeluarBinding

class SuratKeluarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentSuratKeluarBinding? = null
    private val find get() = _find!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentSuratKeluarBinding.inflate(inflater, container, false)

        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
        val username = sheredPreferences.getString("username", null)
        find.tvUsername.setText(username.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }

}