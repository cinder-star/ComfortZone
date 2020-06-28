package com.sihan.comfortzone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sihan.comfortzone.R

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBar: ChipNavigationBar
    private lateinit var materialSearchView: MaterialSearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        bindListeners()
    }

    private fun bindListeners() {
        materialSearchView.setOnQueryTextListener(object: MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }

        })
    }

    private fun bindWidgets() {
        navigationBar = findViewById(R.id.nav_bar)
        navigationBar.setItemSelected(0)
        materialSearchView = findViewById(R.id.my_search_bar)
        materialSearchView.closeSearch()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}