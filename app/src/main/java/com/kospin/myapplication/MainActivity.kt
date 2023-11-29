package com.kospin.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.kospin.myapplication.databinding.ActivityMainBinding
import com.kospin.myapplication.fragment.DashboardFragment
import com.kospin.myapplication.viewModel.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var find : ActivityMainBinding
    private var backButtonPressedTime = 0L
    private val backButtonThreshold = 3000L
    private val myViewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityMainBinding.inflate(layoutInflater)
        setContentView(find.root)

        val navCtrl = this.findNavController(R.id.navhost_fragment)
        NavigationUI.setupWithNavController(find.bottomNavigationView, navCtrl)
        val data = intent.getStringExtra("username")
        myViewModel.setUser(data.toString())

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