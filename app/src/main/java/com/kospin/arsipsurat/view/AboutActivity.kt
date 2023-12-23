package com.kospin.arsipsurat.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.databinding.ActivityAboutBinding
import com.kospin.arsipsurat.utils.PublicFunction

class AboutActivity : AppCompatActivity() {

    private lateinit var find : ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(find.root)

        val version = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).versionName!!
        find.tvVersionApp.setText("versi : $version")

        find.btnAboutBack.setOnClickListener {
            onBackPressed()
        }
        find.linkDev1.setOnClickListener {
            val medsos = arrayOf("riqmuq106@gmail.com", "riqzamuqtada", "rqzx.kaisel106")
            pilihMedsos(medsos, find.linkDev1)
        }
        find.linkDev2.setOnClickListener {
            val medsos = arrayOf("natairnanhtif@gmail.com", "fithnanriatan", "riatanfithnan")
            pilihMedsos(medsos, find.linkDev2)
        }
        find.linkDev3.setOnClickListener {
            val medsos = arrayOf("naylalizzahnaela@gmail.com", "hfnaylaa", "hfnaylaa")
            pilihMedsos(medsos, find.linkDev3)
        }
        find.linkDev4.setOnClickListener {
            val medsos = arrayOf("essacitraadelia@gmail.com", "essacitra", "ssy.now")
            pilihMedsos(medsos, find.linkDev4)
        }

    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.from_down, R.anim.pinch_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.pinch_in, R.anim.to_down)
    }

    private fun pilihMedsos(medsos: Array<String>, view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.chose_medsos, popupMenu.menu)

        // Set listener untuk menangani item yang dipilih
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.email -> {
                    // Lakukan aksi untuk "Tentang App"
                    openEmailProfile(medsos[0])
                    true
                }
                R.id.github -> {
                    // Lakukan aksi untuk "Hapus Sampah"
                    openGitHubProfile(medsos[1])
                    true
                }
                R.id.ing -> {
                    openInstagramProfile(medsos[2])
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun openEmailProfile(emailAddress: String) {
        // Membuat Intent dengan action ACTION_SEND
        val emailIntent = Intent(Intent.ACTION_SEND)

        // Menambahkan alamat email tujuan
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))

        // Menentukan tipe MIME untuk intent
        emailIntent.type = "message/rfc822"

        // Mengecek apakah terdapat aplikasi email yang dapat menangani Intent
        if (emailIntent.resolveActivity(packageManager) != null) {
            // Menampilkan daftar aplikasi email yang tersedia
            startActivity(Intent.createChooser(emailIntent, "Pilih Aplikasi Email"))
        } else {
            // Handle jika tidak terdapat aplikasi email yang dapat menangani Intent
            PublicFunction.alert("Anda tidak memiliki aplikasi email", this)
        }
    }

    private fun openGitHubProfile(username: String) {
        val uri = Uri.parse("https://github.com/$username")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Jika aplikasi GitHub sudah diinstal, buka profil menggunakan aplikasi tersebut
        intent.setPackage("com.github.android")

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Jika aplikasi GitHub tidak diinstal, buka profil menggunakan browser
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun openInstagramProfile(username: String) {
        val uri = Uri.parse("http://instagram.com/_u/$username")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Jika Instagram sudah diinstal, buka profil menggunakan aplikasi Instagram
        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Jika Instagram tidak diinstal, buka profil menggunakan browser
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$username")))
        }
    }

}