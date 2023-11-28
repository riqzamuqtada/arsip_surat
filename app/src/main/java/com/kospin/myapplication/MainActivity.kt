package com.kospin.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.kospin.myapplication.databinding.ActivityMainBinding
import com.kospin.myapplication.fragment.DashboardFragment
import com.kospin.myapplication.viewModel.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var find : ActivityMainBinding
    private val myViewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityMainBinding.inflate(layoutInflater)
        setContentView(find.root)

        val navCtrl = this.findNavController(R.id.navhost_fragment)
        NavigationUI.setupWithNavController(find.bottomNavigationView, navCtrl)
        val data = intent.getStringExtra("username")
        myViewModel.setUser(data!!)

//        val user = intent.getStringExtra("username")
//        val data = DashboardFragment.newInstance(user!!)
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.navhost_fragment, data)
//            .addToBackStack(null)
//            .commit()

    }
}