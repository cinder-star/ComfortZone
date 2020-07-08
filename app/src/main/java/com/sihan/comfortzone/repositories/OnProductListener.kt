package com.sihan.comfortzone.repositories

import com.sihan.comfortzone.domains.Product

interface OnProductListener {
    fun onProductClicked(product: Product)
}