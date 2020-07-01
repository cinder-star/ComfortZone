package com.sihan.comfortzone.database

import com.sihan.comfortzone.domains.Product

class DataManager<T>() {
    private var items: ArrayList<Product> = arrayListOf()

    init {
        items.add(Product(1,"Rice", 200))
        items.add(Product(2,"Water", 50))
        items.add(Product(3,"Fish", 150))
    }
    fun getItems(): ArrayList<Product> = items
}