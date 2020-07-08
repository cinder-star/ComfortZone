package com.sihan.comfortzone.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.repositories.MyAdapter

class CategoryProduct(private val path: String, private val child: String, private val value: String) {
    fun addListener(items: MutableList<Product>, adapter: MyAdapter) {
        val databaseReference = Firebase.database.reference.child(path)
        databaseReference.orderByChild(child).equalTo(value).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                snapshot.children.forEach{
                    val product: Product = it.getValue<Product>() as Product
                    items.add(product)
                }
                adapter.setItem(items)
                adapter.dataChanged()
            }

        })
    }

}