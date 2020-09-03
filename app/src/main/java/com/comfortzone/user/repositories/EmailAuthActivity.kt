package com.comfortzone.user.repositories

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.comfortzone.user.activities.MainActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


abstract class EmailAuthActivity(private var xmlId: Int) : AppCompatActivity() {
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(xmlId)
        bindWidgets()
        bindListeners()
    }

    abstract fun bindWidgets()

    abstract fun bindListeners()

    fun signUpUser(p0: String, p1: String, p2: ProgressBar) {
        auth.createUserWithEmailAndPassword(p0, p1).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                p2.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                p2.visibility = View.GONE
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    Toast.makeText(
                        this,
                        "Password too weak! must be at least 6 characters.",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this, "Invalid email!", Toast.LENGTH_LONG).show()
                } catch (e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(
                        this,
                        "User with this credential already exists!",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this, e.message!!, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun logInUser(p0: String, p1: String, p2: ProgressBar) {
        auth.signInWithEmailAndPassword(p0, p1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    p2.visibility = View.GONE
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    p2.visibility = View.GONE
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Toast.makeText(
                            this,
                            "Password too weak! must be at least 6 characters.",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid email!", Toast.LENGTH_LONG).show()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Toast.makeText(
                            this,
                            "User with this credential already exists!",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message!!, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
}