package com.comfortzone.user.domains

data class Category(
    val id: String? = null,
    val name: String? = null,
    val imagePath: String? = null,
    val subCategory: String? = "no",
    val lastModified: String? = null
)