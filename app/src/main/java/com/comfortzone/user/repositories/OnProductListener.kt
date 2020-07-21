package com.comfortzone.user.repositories

import com.comfortzone.user.domains.Product

interface OnProductListener {
    fun onProductClicked(product: Product)
}