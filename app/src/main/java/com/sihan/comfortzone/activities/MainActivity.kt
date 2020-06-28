package com.sihan.comfortzone.activities

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sihan.comfortzone.R

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBar: ChipNavigationBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var materialSearchView: MaterialSearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        bindListeners()
    }

    private fun bindListeners() {
        materialSearchView.setEllipsize(true)
        materialSearchView.setOnQueryTextListener(object: MaterialSearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val i = Intent(this@MainActivity, SearchActivity::class.java)
                i.putExtra(SearchManager.QUERY, query)
                i.action = Intent.ACTION_SEARCH
                startActivity(i)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        materialSearchView.setOnSearchViewListener(object: MaterialSearchView.SearchViewListener{
            override fun onSearchViewClosed() {
            }

            override fun onSearchViewShown() {
            }

        })
    }

    private fun bindWidgets() {
        navigationBar = findViewById(R.id.nav_bar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_drawer)
        toolbar = findViewById(R.id.toolbar)

        navigationBar.setItemSelected(R.id.home)
        setSupportActionBar(toolbar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
            )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        materialSearchView = findViewById(R.id.my_search_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.action_search)
        materialSearchView.setMenuItem(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (materialSearchView.isSearchOpen) {
            materialSearchView.closeSearch()
        } else {
            finishAffinity()
        }
    }
}