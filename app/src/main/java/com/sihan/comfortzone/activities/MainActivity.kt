package com.sihan.comfortzone.activities

import android.app.SearchManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.fragments.CartFragment
import com.sihan.comfortzone.fragments.ProductFragment
import io.paperdb.Paper


class MainActivity : AppCompatActivity(){
    private lateinit var navigationBar: ChipNavigationBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var materialSearchView: MaterialSearchView
    private var bundle: Bundle = Bundle()
    lateinit var stack: MyStack<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        bindListeners()
        loadInitialFragment()
    }

    private fun loadInitialFragment() {
        stack.clear()
        stack.push("productFragment")
        bundle.putSerializable("stack", stack)
        val productFragment = ProductFragment()
        productFragment.arguments = bundle
        loadFragment(productFragment)
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
        prepareBottomNavBar()
        prepareHamburgerMenu()
    }

    private fun prepareHamburgerMenu() {
        navigationView.setNavigationItemSelectedListener { item ->
            selectDrawerItem(item)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun selectDrawerItem(item: MenuItem) {
        when(item.itemId) {
            R.id.home_drawer -> {
                stack.clear()
                putOnStack("productFragment")
                val productFragment = ProductFragment()
                productFragment.arguments = bundle
                item.isChecked = true
                navigationBar.setItemSelected(R.id.nav_home)
                loadFragment(productFragment)
            }
            R.id.cart_drawer -> {
                putOnStack("cartFragment")
                val cartFragment = CartFragment()
                cartFragment.arguments = bundle
                item.isChecked = true
                navigationBar.setItemSelected(R.id.cart)
                loadFragment(cartFragment)
            }
            R.id.picture_order -> {
                val i = Intent(this, PhotoOrderActivity::class.java)
                startActivity(i)
            }
        }
        drawerLayout.closeDrawers()
    }

    private fun bindWidgets() {
        navigationBar = findViewById(R.id.nav_bar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_drawer)
        toolbar = findViewById(R.id.toolbar)
        stack = MyStack()

        navigationBar.setItemSelected(R.id.nav_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_nav,
                R.string.close_nav
            )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()
        navigationView.setCheckedItem(R.id.home_drawer)
        materialSearchView = findViewById(R.id.my_search_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.action_search)
        materialSearchView.setMenuItem(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return when(item.itemId) {
            R.id.action_search -> true
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (materialSearchView.isSearchOpen) {
            materialSearchView.closeSearch()
        } else {
            stack.pop()
            super.onBackPressed()
        }
    }

    private fun prepareBottomNavBar(){
        navigationBar.setOnItemSelectedListener(object: ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                when(id) {
                    R.id.nav_home -> {
                        stack.clear()
                        putOnStack("productFragment")
                        val productFragment = ProductFragment()
                        productFragment.arguments = bundle
                        navigationView.setCheckedItem(R.id.home_drawer)
                        loadFragment(productFragment)
                    }
                    R.id.cart -> {
                        putOnStack("cartFragment")
                        val cartFragment = CartFragment()
                        cartFragment.arguments = bundle
                        navigationView.setCheckedItem(R.id.cart_drawer)
                        loadFragment(cartFragment)
                    }
                }
            }
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_holder, fragment)
        manager.addToBackStack(null)
        manager.commit()
    }

    private fun putOnStack(string: String) {
        val top = stack.peek()
        if (top != null && string != stack.peek()) {
            stack.push(string)
        }
    }
}