package com.kospin.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kospin.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var find : ActivityLoginBinding
//    private lateinit var myViewModel : MyViewModel
    private val REQUEST_CODE_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(find.root)

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
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .putExtra("username", inputUsername.text.toString())
                    )
                    finish()
                    alert("Hello ${inputUsername.text}")
                } else{
                    alert("Username dan Password salah")
                }
            } else {
                alert("Username dan Password tidak boleh kosong")
            }
        }

    }

    private fun alert(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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