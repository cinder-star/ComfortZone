package com.sihan.comfortzone.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.fragments.*


class MainActivity : AppCompatActivity() {
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
        materialSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) {
                    Toast.makeText(this@MainActivity, "Nothing to search!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (!stack.peek()!!.startsWith("searchActivity")) {
                        loadFragment(SearchFragment())
                        stack.push("searchActivity_$query")
                    } else {
                        stack.pop()
                        stack.push("searchActivity_$query")
                        val fragment =
                            supportFragmentManager.findFragmentById(R.id.fragment_holder) as SearchFragment
                        fragment.prepareSearchResult()
                    }
                    materialSearchView.closeSearch()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        materialSearchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                materialSearchView.hideKeyboard()
            }

            override fun onSearchViewShown() {}
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
        when (item.itemId) {
            R.id.home_drawer -> {
                item.isChecked = true
                stack.clear()
                stack.push("productFragment")
                navigationBar.setItemSelected(R.id.nav_home)
                loadFragment(ProductFragment())
            }
            R.id.cart_drawer -> {
                putOnStack("cartFragment")
                item.isChecked = true
                navigationBar.setItemSelected(R.id.cart)
                loadFragment(CartFragment())
            }
            R.id.drawer_feedback -> {
                stack.push("feedbackFragment")
                navigationBar.setItemSelected(R.id.nav_feedback)
                loadFragment(FeedbackFragment())
            }
            R.id.drawer_dev -> {
                stack.push("devFragment")
                loadFragment(DevInfoFragment())
            }
            R.id.sign_out_drawer -> {
                Firebase.auth.signOut()
                LoginManager.getInstance().logOut()
                val i = Intent(this, ChooserActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        return when (item.itemId) {
            R.id.action_search -> true
            R.id.photo_order -> {
                val i = Intent(this, PhotoOrderActivity::class.java)
                startActivity(i)
                true
            }
            R.id.call_us -> {
                val phone = "+8801922227400"
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                this.startActivity(intent)
                true
            }
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
            var fragName = stack.peek()
            if (fragName == "productFragment") {
                AlertDialog.Builder(this)
                    .setMessage("আপনি কি অ্যাপ থেকে প্রস্থান করতে চান?")
                    .setPositiveButton("হ্যাঁ") { _, _ ->
                        finishAffinity()
                    }
                    .setNegativeButton("না", null)
                    .show()
            } else {
                stack.pop()
                fragName = stack.peek()
                if (fragName!!.startsWith("searchActivity")) {
                    super.onBackPressed()
                    supportFragmentManager.popBackStackImmediate()
                }
                else {
                    when (fragName) {
                        "productFragment" -> {
                            super.onBackPressed()
                            syncFragments(R.id.nav_home, R.id.home_drawer)
                        }
                        "cartFragment" -> {
                            super.onBackPressed()
                            syncFragments(R.id.cart, R.id.cart_drawer)
                        }
                        "orderFragment" -> {
                            super.onBackPressed()
                            syncFragments(R.id.cart, R.id.cart_drawer)
                        }
                        "feedbackFragment" -> {
                            super.onBackPressed()
                            syncFragments(R.id.nav_feedback, R.id.drawer_feedback)
                        }
                        "devFragment" -> {
                            super.onBackPressed()
                            navigationView.setCheckedItem(R.id.drawer_dev)
                        }
                        else -> {
                            super.onBackPressed()
                        }
                    }
                }
            }
        }
    }

    private fun syncFragments(bottomId: Int, humBurgerId: Int) {
        navigationBar.setItemSelected(bottomId)
        navigationView.setCheckedItem(humBurgerId)
    }

    private fun prepareBottomNavBar() {
        navigationBar.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.nav_home -> {
                        stack.clear()
                        stack.push("productFragment")
                        navigationView.setCheckedItem(R.id.home_drawer)
                        loadFragment(ProductFragment())
                    }
                    R.id.cart -> {
                        putOnStack("cartFragment")
                        navigationView.setCheckedItem(R.id.cart_drawer)
                        loadFragment(CartFragment())
                    }
                    R.id.nav_feedback -> {
                        stack.push("feedbackFragment")
                        navigationView.setCheckedItem(R.id.drawer_feedback)
                        loadFragment(FeedbackFragment())
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
        fragment.arguments = bundle
        val manager = supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_holder, fragment)
        manager.addToBackStack(null)
        manager.commit()
    }

    private fun putOnStack(string: String) {
        val top = stack.peek()
        if (top == null || string != stack.peek()) {
            stack.push(string)
        }
    }

    private fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}