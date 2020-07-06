package com.sihan.comfortzone.repositories

import androidx.recyclerview.widget.RecyclerView

interface MyAdapter{
    fun setItem(items: List<*>)
    fun dataChanged()
}