package com.comfortzone.user.domains

data class Order(
    val id: String? = null,
    val ownerId: String? = null,
    val totalPrice: Double? = 0.0,
    val name: String? = null,
    val address: String? = null,
    val mobileNumber: String? = null,
    val completed: String? = "no"
)