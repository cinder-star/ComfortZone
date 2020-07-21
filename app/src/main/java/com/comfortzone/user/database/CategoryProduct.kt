package com.comfortzone.user.database

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.comfortzone.user.domains.Product
import com.comfortzone.user.repositories.MyAdapter

class CategoryProduct(
    private val path: String,
    private val child: String,
    private val value: String
) {
    fun addListener(adapter: MyAdapter) {
        val databaseReference = Firebase.database.reference.child(path)
        val items = mutableListOf<Product>()
        databaseReference.orderByChild(child).equalTo(value)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    items.clear()
                    snapshot.children.forEach {
                        val product: Product = it.getValue<Product>() as Product
                        items.add(product)
                    }
                    Log.e("query", items.toString())
                    adapter.setItem(items)
                    adapter.dataChanged()
                }

            })
    }

}