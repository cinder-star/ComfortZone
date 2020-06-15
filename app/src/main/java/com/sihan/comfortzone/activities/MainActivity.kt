package com.sihan.comfortzone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sihan.comfortzone.R
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}