package com.kospin.arsipsurat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.kospin.arsipsurat.databinding.ActivityMainBinding
import com.kospin.arsipsurat.model.InputActivity

class MainActivity : AppCompatActivity() {

    private lateinit var find : ActivityMainBinding
    private var backButtonPressedTime = 0L
    private val backButtonThreshold = 2400L
    private var currentFragmentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityMainBinding.inflate(layoutInflater)
        setContentView(find.root)

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_blue_light))

        val navController = Navigation.findNavController(this, R.id.navhost_fragment)
        currentFragmentId = R.id.dashboardFragment

        find.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            when (item.itemId) {
                R.id.dashboardFragment -> {
                    if (currentFragmentId != R.id.dashboardFragment) {
                        currentFragmentId = R.id.dashboardFragment
                        navController.navigate(R.id.dashboardFragment, null, navOptions)
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                R.id.allSuratFragment -> {
                    if (currentFragmentId != R.id.allSuratFragment) {
                        currentFragmentId = R.id.allSuratFragment
                        navController.navigate(R.id.allSuratFragment, null, navOptions)
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                R.id.inputActivity -> {
                    startActivity(Intent(this, InputActivity::class.java))
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.suratMasukFragment -> {
                    if (currentFragmentId != R.id.suratMasukFragment) {
                        currentFragmentId = R.id.suratMasukFragment
                        navController.navigate(R.id.suratMasukFragment, null, navOptions)
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                R.id.suratKeluarFragment -> {
                    if (currentFragmentId != R.id.suratKeluarFragment) {
                        currentFragmentId = R.id.suratKeluarFragment
                        navController.navigate(R.id.suratKeluarFragment, null, navOptions)
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        if (currentFragmentId == R.id.dashboardFragment) {
            if (backButtonPressedTime + backButtonThreshold > System.currentTimeMillis()) {
                finish()
            } else {
                Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
            }
            backButtonPressedTime = System.currentTimeMillis()
        } else {
            find.bottomNavigationView.selectedItemId = R.id.dashboardFragment
        }
    }
}