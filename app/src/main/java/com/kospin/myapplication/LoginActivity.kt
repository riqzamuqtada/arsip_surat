package com.kospin.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kospin.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var find : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(find.root)

        val username = "admin"
        val password = "kospin123"
        val inputUsername = find.etUsername
        val inputPassword = find.etPassword

        find.btnLogin.setOnClickListener {
            if (inputUsername.text.isNotEmpty() && inputPassword.text.isNotEmpty()){
                if (inputUsername.text.toString() == username && inputPassword.text.toString() == password){
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .putExtra("username", inputUsername.text.toString())
                    )
                    finish()
                    alert("Hello Admin")
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
}