package com.sihan.comfortzone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R
import io.paperdb.Paper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Paper.init(this)
        Firebase.database.setPersistenceEnabled(true)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            checkStatus()
        }, 750)
    }

    private fun checkStatus() {
        val status = Paper.book().read("status", 0)
        if (status == 0) {
            Paper.book().write("status", 1)
            val i = Intent(this, SliderActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        } else{
            if (Firebase.auth.currentUser == null) {
                val i = Intent(this, ChooserActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            } else {
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }
    }
}