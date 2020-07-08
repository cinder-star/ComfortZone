package com.sihan.comfortzone.repositories

import com.sihan.comfortzone.domains.Category

interface OnCategoryListener {
    fun onCategoryClicked(category: Category)
}