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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(find.root)

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_blue_dark))

        val username = "admin"
        val password = "kospin123"
        val inputUsername = find.etUsername
        val inputPassword = find.etPassword

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

}