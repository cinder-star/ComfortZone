package com.comfortzone.user.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.comfortzone.user.domains.Product
import com.comfortzone.user.domains.ShoppingCart
import com.comfortzone.user.repositories.MyAdapter

class DataManager(var path: String) {
    inline fun <reified P : Any> setListener(adapter: MyAdapter) {
        val items: MutableList<P> = arrayListOf()
        val reference: DatabaseReference = Firebase.database.reference.child(path)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                snapshot.children.forEach {
                    val t: P? = it.getValue<P>()
                    items.add(t!!)
                    if (t is Product) {
                        ShoppingCart.updateItem(t)
                    }
                }
                adapter.setItem(items)
                adapter.dataChanged()
            }

        })
    }
}