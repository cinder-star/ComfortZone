package com.sihan.comfortzone.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.PagerAdapter
import com.sihan.comfortzone.R

class MyPager(private val context: Context): PagerAdapter() {
    private val ids: IntArray =
        intArrayOf(R.drawable.ic_slide_1, R.drawable.ic_slide_2, R.drawable.ic_slide_3)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun getCount(): Int {
        return ids.size
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_onboarding_slider_page, null)
        val imageView: ImageView = view.findViewById(R.id.slider_image)
        imageView.setImageDrawable(ResourcesCompat.getDrawable(context.resources, getImageAt(position), null))
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun getImageAt(@Suppress("SameParameterValue") position: Int): Int {
        return ids[position]
    }
}