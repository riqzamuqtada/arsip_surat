package com.kospin.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.kospin.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var find : ActivityMainBinding
    private var backButtonPressedTime = 0L
    private val backButtonThreshold = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityMainBinding.inflate(layoutInflater)
        setContentView(find.root)

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_blue_light))

        val navCtrl = this.findNavController(R.id.navhost_fragment)
        NavigationUI.setupWithNavController(find.bottomNavigationView, navCtrl)


    }

    override fun onBackPressed() {
        val navCtrl = this.findNavController(R.id.navhost_fragment)
        if (navCtrl.currentDestination?.id == R.id.dashboardFragment){
            if (backButtonPressedTime + backButtonThreshold > System.currentTimeMillis()) {
                super.onBackPressed()
            } else {
                Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
            }
            backButtonPressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }
}