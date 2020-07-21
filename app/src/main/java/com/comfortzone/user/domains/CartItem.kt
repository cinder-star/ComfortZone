package com.comfortzone.user.domains

data class CartItem(
    var product: Product,
    var quantity: Int = 0
)