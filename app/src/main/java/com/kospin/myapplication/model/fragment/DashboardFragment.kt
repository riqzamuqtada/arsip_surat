package com.kospin.myapplication.model.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.kospin.myapplication.R
import com.kospin.myapplication.model.LoginActivity
import com.kospin.myapplication.databinding.FragmentDashboardBinding
import com.kospin.myapplication.utils.PublicFunction
import com.kospin.myapplication.viewmodel.SuratViewModel
import java.io.File

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _find: FragmentDashboardBinding? = null
    private val find get() = _find!!

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

        find.btnDashboardMenu.setOnClickListener { showPopupMenu() }

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

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), find.btnDashboardMenu)
        popupMenu.menuInflater.inflate(R.menu.dashboard_menu_options, popupMenu.menu)

        // Set listener untuk menangani item yang dipilih
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_about_app -> {
                    // Lakukan aksi untuk "Tentang App"
                    true
                }
                R.id.menu_clear_trash -> {
                    // Lakukan aksi untuk "Hapus Sampah"
                    dialogDelete()
                    true
                }
                R.id.menu_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun dialogDelete() {

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah Anda yakin ingin keluar dari akun Anda? Keluar akan mengakhiri sesi.")

        builder.setPositiveButton("Hapus"){ dialog, _ ->
         deleteAllTemporaryFiles()
        }

        builder.setNegativeButton("Batal"){ dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()

        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.clr_red))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_hard))

    }

    private fun deleteAllTemporaryFiles() {
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return

        // Filter file yang memiliki nama yang sesuai dengan pola file temporer
        val temporaryFiles = storageDir.listFiles { file ->
            file.name.startsWith("JPEG_") && file.name.endsWith(".jpg")
        }

        // Hapus semua file temporer
        temporaryFiles?.forEach { file ->
            file.delete()
        }
    }


    private fun logout(){
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah Anda yakin ingin keluar dari akun Anda? Keluar akan mengakhiri sesi.")

        builder.setPositiveButton("Logout"){ dialog, _ ->
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            val sheredPreferences = requireActivity().getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
            sheredPreferences.edit().clear().apply()
            requireActivity().finish()
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal"){ dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()

        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.clr_red))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_hard))

    }

    private fun viewModel(): SuratViewModel {
        return PublicFunction.getSuratViewModel(requireContext())
    }

    private fun setChartOrange(entries: List<PieEntry>) {
        val pieChart = find.cpSuratAll
        val dataSet = PieDataSet(entries, null)

        val mainBlue    = ContextCompat.getColor(requireContext(), R.color.main_blue_dark)
        val mainWhite   = ContextCompat.getColor(requireContext(), R.color.main_white_dark)

        val colors      = listOf(mainBlue, mainWhite)
        dataSet.setDrawValues(false)
        dataSet.colors = colors

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

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // Potongan dipilih, tampilkan nilai
                val index = entries.indexOf(e as PieEntry)
                val selectedColor = colors[index % colors.size]
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                pieChart.setCenterTextTypeface(boldTypeface)
                pieChart.centerText = "${e.y.toInt()}"
                pieChart.setCenterTextColor(selectedColor)
                pieChart.setCenterTextSize(16f)
            }

            override fun onNothingSelected() {
                // Potongan tidak dipilih, sembunyikan nilai
                pieChart.centerText = ""
            }
        })

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

        val colors = listOf(rnbwGreen, rnbwBlue, rnbwRed, rnbwPurple, rnbwYellow, rnbwOrange)

        dataSet.setDrawValues(false)
        dataSet.selectionShift = 6f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
        dataSet.colors = colors

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

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // Potongan dipilih, tampilkan nilai
                val index = entries.indexOf(e as PieEntry)
                val selectedColor = colors[index % colors.size]
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                pieChart.setCenterTextTypeface(boldTypeface)
                pieChart.centerText = "${e.y.toInt()}"
                pieChart.setCenterTextColor(selectedColor)
                pieChart.setCenterTextSize(16f)
            }

            override fun onNothingSelected() {
                // Potongan tidak dipilih, sembunyikan nilai
                pieChart.centerText = ""
            }
        })

        pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }
}