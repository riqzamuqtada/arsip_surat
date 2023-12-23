package com.kospin.arsipsurat.model.fragment

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
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
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.model.LoginActivity
import com.kospin.arsipsurat.databinding.FragmentDashboardBinding
import com.kospin.arsipsurat.model.AboutActivity
import com.kospin.arsipsurat.utils.PublicFunction
import com.kospin.arsipsurat.viewmodel.SuratViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        viewModel().username.observe(viewLifecycleOwner, Observer {
            find.tvDashboardUsername.setText(it)
        })

        find.btnDashboardMenu.setOnClickListener { showPopupMenu() }

        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .build()
        find.btnToSrtMasuk.setOnClickListener {
            findNavController().navigate(R.id.toSuratMasuk, null, navOptions)
            viewModel().setIdFragment(R.id.suratMasukFragment)
        }
        find.btnToSrtKeluar.setOnClickListener {
            findNavController().navigate(R.id.toSuratKeluar, null, navOptions)
            viewModel().setIdFragment(R.id.suratKeluarFragment)
        }
        find.btnToAllSrt.setOnClickListener {
            findNavController().navigate(R.id.toAllSurat, null, navOptions)
            viewModel().setIdFragment(R.id.allSuratFragment)
        }

//        pieChart jenis surat
        viewModel().dataPieJenis.observe(viewLifecycleOwner, Observer { data ->
            setChartOrange(data)
        })

//        pieChart surat masuk
        viewModel().dataPieMasuk.observe(viewLifecycleOwner, Observer { data ->
            setChartBlue(true, data)
        })

//        pieChart surat keluar
        viewModel().dataPieKeluar.observe(viewLifecycleOwner, Observer { data ->
            setChartBlue(false, data)
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
                    aboutappPage()
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

    private fun aboutappPage() {
        val intent = Intent(activity, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun dialogDelete() {

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Konfirmasi Hapus Sampah")
        builder.setMessage("Apakah Anda yakin ingin menghapus sampah dan membersihkan cache? Tindakan ini akan membebaskan ruang penyimpanan.")

        builder.setPositiveButton("Hapus dan Bersihkan"){ dialog, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                deleteAllTemporaryFiles()
                clearAppCache(requireContext())
                PublicFunction.alert("Cache dan sampah telah berhasil dihapus. Ruang penyimpanan kini lebih optimal. \uD83D\uDE80", requireContext())
            }
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
    private fun clearAppCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            val cacheDirectory = File(cacheDir.path)

            if (cacheDirectory.exists()) {
                deleteDir(cacheDirectory)
            }

            context.externalCacheDir?.let { externalCacheDir ->
                val externalCacheDirectory = File(externalCacheDir.path)
                if (externalCacheDirectory.exists()) {
                    deleteDir(externalCacheDirectory)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
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
            dialog.dismiss()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            PublicFunction.alert("Logout Berhasil! Sampai jumpa lagi. \uD83D\uDC4B", requireContext())
            requireActivity().finish()
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
        val white   = ContextCompat.getColor(requireContext(), R.color.white70)

        val colors      = listOf(mainBlue, mainWhite)
        dataSet.setDrawValues(false)
        dataSet.colors = colors

        pieChart.data = PieData(dataSet)
        pieChart.invalidate()
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.animateY(800, Easing.EaseInOutQuad)
        pieChart.setExtraOffsets(32f, 6f, 0f, 6f)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setHoleColor(Color.TRANSPARENT)
        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        pieChart.setCenterTextTypeface(boldTypeface)
        pieChart.setCenterTextColor(white)
        viewModel().getJumlahSurat.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                pieChart.setCenterTextSize(11f)
                pieChart.centerText = "Data kosong"
            } else {
                pieChart.setCenterTextSize(16f)
                pieChart.centerText = "${it.toInt()}"
            }
        })

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
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                pieChart.setCenterTextTypeface(boldTypeface)
                pieChart.setCenterTextColor(white)
                viewModel().getJumlahSurat.observe(viewLifecycleOwner, Observer {
                    if (it == 0) {
                        pieChart.setCenterTextSize(11f)
                        pieChart.centerText = "Data kosong"
                    } else {
                        pieChart.setCenterTextSize(16f)
                        pieChart.centerText = "${it.toInt()}"
                    }
                })
            }
        })

        pieChart.invalidate()
    }

    private fun setChartBlue(jenis: Boolean, entries: List<PieEntry>) {
        val pieChart: PieChart = if (jenis) find.cpSuratMasuk else find.cpSuratKeluar
        val dataSet = PieDataSet(entries, null)

        val rnbwGreen   = ContextCompat.getColor(requireContext(), R.color.rnbw_green)
        val rnbwBlue    = ContextCompat.getColor(requireContext(), R.color.rnbw_blue)
        val rnbwRed     = ContextCompat.getColor(requireContext(), R.color.rnbw_red)
        val rnbwPurple  = ContextCompat.getColor(requireContext(), R.color.rnbw_purple)
        val rnbwYellow  = ContextCompat.getColor(requireContext(), R.color.rnbw_yellow)
        val rnbwOrange  = ContextCompat.getColor(requireContext(), R.color.rnbw_orange)
        val black  = ContextCompat.getColor(requireContext(), R.color.black_light)
        val mainWhite   = ContextCompat.getColor(requireContext(), R.color.main_white_dark)
        val white   = ContextCompat.getColor(requireContext(), R.color.white70)

        val colors = listOf(rnbwGreen, rnbwBlue, rnbwRed, rnbwPurple, rnbwYellow, rnbwOrange, black, mainWhite)

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
        pieChart.setExtraOffsets(16f, 8f, 10f, 8f)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setHoleColor(Color.TRANSPARENT)
        val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
        pieChart.setCenterTextTypeface(boldTypeface)
        pieChart.setCenterTextColor(white)
        val jumlahData = if (jenis) viewModel().jumlahMasuk else viewModel().jumlahKeluar
        jumlahData.observe(viewLifecycleOwner, Observer {
            if (it.toInt() == 0){
                pieChart.setCenterTextSize(12f)
                pieChart.centerText = "Data tidak tersedia"
            } else {
                pieChart.setCenterTextSize(16f)
                pieChart.centerText = "${it.toInt()}"
            }
        })

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
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                pieChart.setCenterTextTypeface(boldTypeface)
                pieChart.setCenterTextColor(white)
                val jumlahData = if (jenis) viewModel().jumlahMasuk else viewModel().jumlahKeluar
                jumlahData.observe(viewLifecycleOwner, Observer {
                    if (it.toInt() == 0){
                        pieChart.setCenterTextSize(12f)
                        pieChart.centerText = "Data tidak tersedia"
                    } else {
                        pieChart.setCenterTextSize(16f)
                        pieChart.centerText = "${it.toInt()}"
                    }
                })
            }
        })

        pieChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _find = null
    }
}