package com.kospin.arsipsurat.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var find : ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnAboutBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.from_down, R.anim.pinch_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.pinch_in, R.anim.to_down)
    }
}