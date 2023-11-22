package com.kospin.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.kospin.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var find : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityMainBinding.inflate(layoutInflater)
        setContentView(find.root)

        val navCtrl = this.findNavController(R.id.navhost_fragment)
        NavigationUI.setupWithNavController(find.bottomNavigationView, navCtrl)

    }
}