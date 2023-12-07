package com.kospin.myapplication.model.mainmodel.fragment

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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kospin.myapplication.R
import com.kospin.myapplication.model.LoginActivity
import com.kospin.myapplication.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentDashboardBinding? = null
    private val find get() = _find!!
    private val allSurat = ArrayList<PieEntry>()
    private val masukSurat = ArrayList<PieEntry>()
    private val keluarSurat = ArrayList<PieEntry>()

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
            val builder = AlertDialog.Builder(this.requireContext())

            builder.setTitle("Logout")
            builder.setMessage("apakah anda yakin ingin Logout")

            builder.setPositiveButton("ya"){ dialog, _ ->
                startActivity(Intent(this.requireContext(), LoginActivity::class.java))
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

        dataListing()

    }

    private fun setChartOrange(pieChart: PieChart, dataSet: PieDataSet, vararg colors: Int) {
        dataSet.setDrawValues(false)
        dataSet.colors = colors.toList()
        dataSet.selectionShift = 6f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val data = PieData(dataSet)
        pieChart.data = data
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
    }

    private fun setChartBlue(pieChart: PieChart, dataSet: PieDataSet, vararg colors: Int) {
        dataSet.setDrawValues(false)
        dataSet.colors = colors.toList()
        dataSet.selectionShift = 6f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

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
    }

    private fun dataListing() {
        val mainBlue    = ContextCompat.getColor(requireContext(), R.color.main_blue_dark)
        val mainWhite   = ContextCompat.getColor(requireContext(), R.color.main_white_dark)
        val rnbwGreen   = ContextCompat.getColor(requireContext(), R.color.rnbw_green)
        val rnbwBlue    = ContextCompat.getColor(requireContext(), R.color.rnbw_blue)
        val rnbwRed     = ContextCompat.getColor(requireContext(), R.color.rnbw_red)
        val rnbwPurple  = ContextCompat.getColor(requireContext(), R.color.rnbw_purple)
        val rnbwYellow  = ContextCompat.getColor(requireContext(), R.color.rnbw_yellow)
        val rnbwOrange  = ContextCompat.getColor(requireContext(), R.color.rnbw_orange)

        // Diagram All Surat
        allSurat.clear()
        allSurat.add(PieEntry(60.toFloat(), "Surat Masuk",))
        allSurat.add(PieEntry(40.toFloat(), "Surat Keluar"))
        val suratAllDataSet = PieDataSet(allSurat, null)
        setChartOrange(find.cpSuratAll, suratAllDataSet, mainBlue, mainWhite)

        // Diagram Surat Masuk
        masukSurat.clear()
        masukSurat.add(PieEntry(7f, "Pengurus"))
        masukSurat.add(PieEntry(3f, "Div. Operasional"))
        masukSurat.add(PieEntry(9f, "Div. Pinjaman"))
        masukSurat.add(PieEntry(12f, "Div. Dana"))
        masukSurat.add(PieEntry(13f, "Div. Pengawasan"))
        masukSurat.add(PieEntry(24f, "Kesekretariatan"))
        val suratMasukDataSet = PieDataSet(masukSurat, null)
        setChartBlue(find.cpSuratMasuk, suratMasukDataSet, rnbwGreen, rnbwBlue, rnbwRed, rnbwPurple, rnbwYellow, rnbwOrange)

        // Diagram Surat Keluar
        keluarSurat.clear()
        keluarSurat.add(PieEntry(23f, "Pengurus"))
        keluarSurat.add(PieEntry(21f, "Div. Operasional"))
        keluarSurat.add(PieEntry(17f, "Div. Pinjaman"))
        keluarSurat.add(PieEntry(57f, "Div. Dana"))
        keluarSurat.add(PieEntry(83f, "Div. Pengawasan"))
        keluarSurat.add(PieEntry(34f, "Kesekretariatan"))
        val suratKeluarDataSet = PieDataSet(keluarSurat, null)
        setChartBlue(find.cpSuratKeluar, suratKeluarDataSet, rnbwGreen, rnbwBlue, rnbwRed, rnbwPurple, rnbwYellow, rnbwOrange)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }
}