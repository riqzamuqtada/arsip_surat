package com.kospin.arsipsurat.model

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.databinding.ActivityLoginBinding
import com.kospin.arsipsurat.utils.PublicFunction

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
                    overridePendingTransition(R.anim.fade_in, R.anim.to_right)
                    val sheredPreferences = getSharedPreferences("sheredFile", Context.MODE_PRIVATE)
                    sheredPreferences.edit().putString("username", inputUsername.text.toString()).apply()
                    finish()
                    PublicFunction.alert("Login Berhasil! Selamat datang ${inputUsername.text} \uD83C\uDF89", this)
                } else{
                    PublicFunction.alert("Username atau Password salah!", this)
                }
            } else {
                PublicFunction.alert("Username atau Password tidak boleh Kosong!", this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.from_left, R.anim.solid)
    }

}