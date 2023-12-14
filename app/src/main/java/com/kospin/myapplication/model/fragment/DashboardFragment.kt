package com.kospin.myapplication.model.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.kospin.myapplication.R
import com.kospin.myapplication.model.LoginActivity
import com.kospin.myapplication.databinding.FragmentDashboardBinding
import com.kospin.myapplication.utils.PublicFunction
import com.kospin.myapplication.viewmodel.SuratViewModel

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentDashboardBinding? = null
    private val find get() = _find!!
    private val masuk = "Masuk"
    private val keluar = "Keluar"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _find = FragmentDashboardBinding.inflate(inflater, container, false)
        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
        val username = sheredPreferences.getString("username", null)
        find.tvDashboardUsername.setText(username.toString())
        find.btnDashboardLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Logout")
            builder.setMessage("apakah anda yakin ingin Logout")

            builder.setPositiveButton("ya"){ dialog, _ ->
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
                sheredPreferences.edit().clear().apply()
                requireActivity().finish()
                dialog.dismiss()
            }

            builder.setNegativeButton("tidak"){ dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }

//        pieChart jenis surat
        viewModel().dataPieJenis.observe(viewLifecycleOwner, Observer { data ->
            setChartOrange(data)
        })

//        pieChart surat masuk
        viewModel().dataPieMasuk.observe(viewLifecycleOwner, Observer { data ->
            setChartBlue(find.cpSuratMasuk, data)
        })

//        pieChart surat keluar
        viewModel().dataPieKeluar.observe(viewLifecycleOwner, Observer { data ->
            setChartBlue(find.cpSuratKeluar, data)
        })

    }

    private fun viewModel(): SuratViewModel {
        return PublicFunction.getSuratViewModel(requireContext())
    }

    private fun setChartOrange(entries: List<PieEntry>) {
        val pieChart = find.cpSuratAll
        val dataSet = PieDataSet(entries, null)

        val mainBlue    = ContextCompat.getColor(requireContext(), R.color.main_blue_dark)
        val mainWhite   = ContextCompat.getColor(requireContext(), R.color.main_white_dark)

        dataSet.setDrawValues(false)
        dataSet.colors = listOf(mainBlue, mainWhite)

        pieChart.data = PieData(dataSet)
        pieChart.invalidate()
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.animateY(800, Easing.EaseInOutQuad)
        pieChart.setExtraOffsets(18f, 6f, 0f, 6f)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setHoleColor(Color.TRANSPARENT)

        val legend: Legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.textColor = Color.WHITE

        pieChart.invalidate()
    }

    private fun setChartBlue(pieChart: PieChart, entries: List<PieEntry>) {
        val dataSet = PieDataSet(entries, null)

        val rnbwGreen   = ContextCompat.getColor(requireContext(), R.color.rnbw_green)
        val rnbwBlue    = ContextCompat.getColor(requireContext(), R.color.rnbw_blue)
        val rnbwRed     = ContextCompat.getColor(requireContext(), R.color.rnbw_red)
        val rnbwPurple  = ContextCompat.getColor(requireContext(), R.color.rnbw_purple)
        val rnbwYellow  = ContextCompat.getColor(requireContext(), R.color.rnbw_yellow)
        val rnbwOrange  = ContextCompat.getColor(requireContext(), R.color.rnbw_orange)

        dataSet.setDrawValues(false)
        dataSet.colors = listOf(rnbwGreen, rnbwBlue, rnbwRed, rnbwPurple, rnbwYellow, rnbwOrange)

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.animateY(1200, Easing.EaseInOutQuad)
        pieChart.setExtraOffsets(16f, 8f, 0f, 8f)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setHoleColor(Color.TRANSPARENT)

        val legend: Legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.textColor = Color.WHITE

        pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }
}