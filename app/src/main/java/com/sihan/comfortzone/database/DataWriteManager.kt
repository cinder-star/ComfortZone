package com.sihan.comfortzone.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DataWriteManager<T>(private val path: String, private val obj: T) {

    fun write() {
        val database = Firebase.database.reference
        database.child(path).setValue(obj)
    }
}