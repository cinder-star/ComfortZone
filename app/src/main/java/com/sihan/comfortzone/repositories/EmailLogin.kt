package com.sihan.comfortzone.repositories

import android.app.Activity
import android.util.Log
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailLogin(
    private var regex: String,
    private var email_field: EditText,
    private var context: Activity
) : BaseLogin {
    private var firebaseAuth = Firebase.auth
    override fun validate(credential: String): Boolean {
        val validation = regex.toRegex()
        if (!validation.matches(credential)) {
            email_field.error = "Invalid input!"
        }
        return validation.matches(credential)
    }

    override fun login(username: String, password: String) {
        if (validate(username)) {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        Log.e("user", "createUserWithEmail:success")
                    } else {
                        Log.e("user", "createUserWithEmail:failure")
                    }
                }
        }
    }

}