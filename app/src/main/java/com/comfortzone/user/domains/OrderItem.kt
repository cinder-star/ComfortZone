package com.comfortzone.user.domains

data class OrderItem(
    val id: String? = null,
    val name: String? = null,
    val imagePath: String? = null,
    val price: Int? = 0,
    val quantity: Int? = 0,
    val lastModified: String? = null
)