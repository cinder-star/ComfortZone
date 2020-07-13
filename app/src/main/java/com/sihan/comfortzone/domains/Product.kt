package com.sihan.comfortzone.domains

import java.io.Serializable

data class Product(
    var id: String? = null,
    var name: String? = null,
    var price: Int? = 0,
    var prevPrice: Int? = 0,
    var imagePath: String? = null,
    var category: String? = null,
    var popular: String? = "no",
    var special: String? = "no"
) : Serializable