package com.comfortzone.user.repositories

import com.comfortzone.user.domains.Category

interface OnCategoryListener {
    fun onCategoryClicked(category: Category)
}