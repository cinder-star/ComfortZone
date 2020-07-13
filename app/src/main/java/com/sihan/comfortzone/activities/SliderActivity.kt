package com.sihan.comfortzone.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.sihan.comfortzone.R
import com.sihan.comfortzone.utils.MyPager

class SliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var myPager: MyPager
    private lateinit var skip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        bindWidgets()
        bindListeners()
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
