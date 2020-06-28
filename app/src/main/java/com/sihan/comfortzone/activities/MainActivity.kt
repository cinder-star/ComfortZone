package com.sihan.comfortzone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.sihan.comfortzone.R

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBar: ChipNavigationBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        bindListeners()
    }

    private fun bindListeners() {
    }

    private fun bindWidgets() {
        navigationBar = findViewById(R.id.nav_bar)
        navigationBar.setItemSelected(0)
    }


    override fun onBackPressed() {
        finishAffinity()
    }
}