package com.sihan.comfortzone.domains

data class CartItem(
    var product: Product,
    var quantity: Int = 0
)