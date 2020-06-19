package com.sihan.comfortzone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sihan.comfortzone.R

class SliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        viewPager = findViewById(R.id.onboardingSlider)
        val onboardingSliderAdapter = OnboardingSliderAdapter(this)
        viewPager.adapter = onboardingSliderAdapter
    }

    private inner class OnboardingSliderAdapter(manager: FragmentActivity) :
        FragmentStateAdapter(manager) {
        private val ids: IntArray =
            intArrayOf(R.drawable.ic_slide_1, R.drawable.ic_slide_2, R.drawable.ic_slide_3)

        override fun getItemCount(): Int = ids.size
        override fun createFragment(position: Int): Fragment =
            OnboardingSliderPageFragment(position)
    }
}
