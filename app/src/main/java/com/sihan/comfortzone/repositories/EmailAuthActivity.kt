package com.sihan.comfortzone.repositories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.activities.MainActivity

abstract class EmailAuthActivity(private var xmlId: Int) : AppCompatActivity() {
    private var auth =  Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(xmlId)
        bindWidgets()
        bindListeners()
    }

    abstract fun bindWidgets()

    abstract fun bindListeners()

    fun signUpUser(p0: String, p1: String) {
        auth.createUserWithEmailAndPassword(p0, p1).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("email_sign_in", "createUserWithEmail:success")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Log.w("email_sign_in", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logInUser(p0: String, p1: String) {
        auth.signInWithEmailAndPassword(p0, p1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("email_log_in", "signInWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Log.w("email_log_in", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}