package com.sihan.comfortzone.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R
import com.sihan.comfortzone.utils.MyPager

class SliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var myPager: MyPager
    private lateinit var skip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        checkUser()
        bindWidgets()
        bindListeners()
    }

    private fun checkUser() {
        val user = Firebase.auth.currentUser
        if (user != null){
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }

    private fun bindListeners() {
        skip.setOnClickListener {
            startActivity(Intent(this, ChooserActivity::class.java))
        }
    }

    private fun bindWidgets() {
        viewPager = findViewById(R.id.onboardingSlider)
        myPager = MyPager(this)
        viewPager.adapter = myPager
        skip = findViewById(R.id.slider_skip)
    }
}
