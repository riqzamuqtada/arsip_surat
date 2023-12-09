package com.kospin.myapplication.model

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kospin.myapplication.MainActivity
import com.kospin.myapplication.R
import com.kospin.myapplication.databinding.ActivityLoginBinding
import com.kospin.myapplication.utils.PublicFunction

class LoginActivity : AppCompatActivity() {

    private lateinit var find : ActivityLoginBinding
    private val REQUEST_CODE_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(find.root)

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_blue_dark))

        val username = "admin"
        val password = "kospin123"
        val inputUsername = find.etUsername
        val inputPassword = find.etPassword
        checkStoragePermission()
        find.btnIzinSubmit.setOnClickListener {
            checkStoragePermission()
        }
        find.btnLogin.setOnClickListener {
            if (inputUsername.text.isNotEmpty() && inputPassword.text.isNotEmpty()){
                if (inputPassword.text.toString() == password){
                    startActivity(Intent(this, MainActivity::class.java))
                    val sheredPreferences = getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
                    sheredPreferences.edit().putString("username", inputUsername.text.toString()).apply()
                    finish()
                    PublicFunction.alert("Selamat Datang ${inputUsername.text}!", this)
                } else{
                    PublicFunction.alert("Username dan Password salah!", this)
                }
            } else {
                PublicFunction.alert("Username dan Password tidak boleh Kosong!", this)
            }
        }

    }

    // Metode untuk memeriksa dan meminta izin
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Meminta izin
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, lanjutkan dengan operasi membaca file
                // Misalnya, panggil metode untuk membaca file gambar di sini
                find.lyLoginIzin.visibility = View.GONE
            } else {
                // Izin ditolak, beri tahu pengguna atau atasi sesuai kebutuhan
//                Toast.makeText(this, "Izin akses penyimpanan ditolak", Toast.LENGTH_SHORT).show()
                find.lyLoginIzin.visibility = View.VISIBLE
            }
        }
    }

}