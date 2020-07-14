package com.sihan.comfortzone.domains

data class PhotoOrder(
    val id: String? = null,
    val imagePath: String? = null,
    val ownerId: String? = null,
    val name: String? = null,
    val address: String? = null,
    val mobileNumber: String? = null,
    val completed: String? = "no"
)