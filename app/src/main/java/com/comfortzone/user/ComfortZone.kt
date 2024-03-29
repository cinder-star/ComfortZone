package com.comfortzone.user

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper

class ComfortZone : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        Paper.init(this)
    }
}