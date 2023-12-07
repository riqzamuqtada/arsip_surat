package com.kospin.myapplication.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kospin.myapplication.R
import com.kospin.myapplication.databinding.FragmentDashboardBinding
import com.kospin.myapplication.viewModel.MyViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentDashboardBinding? = null
    private val find get() = _find!!
    private val myViewModel: MyViewModel by activityViewModels()
    private var param1: String? = null
    private var param2: String? = null

    private val allSurat = ArrayList<PieEntry>()
    private val masukSurat = ArrayList<PieEntry>()
    private val keluarSurat = ArrayList<PieEntry>()

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
        _find = FragmentDashboardBinding.inflate(inflater, container, false)

        return find.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel.username.observe(viewLifecycleOwner, Observer { username ->
            find.tvDashboardUsername.setText(username.toString())
        })

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String = "") =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}