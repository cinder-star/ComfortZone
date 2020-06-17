package com.sihan.comfortzone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sihan.comfortzone.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}