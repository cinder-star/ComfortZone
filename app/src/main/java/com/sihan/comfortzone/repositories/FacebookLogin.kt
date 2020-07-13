package com.sihan.comfortzone.repositories

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.activities.MainActivity

class FacebookLogin(
    private var activity: Activity,
    private var facebookLogin: LoginButton,
    private var callbackManager: CallbackManager
) {

    fun setup() {
        facebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    handleAccessToken(result?.accessToken)
                }

                override fun onCancel() {
                    Log.e("result", "failed")
                }

                override fun onError(error: FacebookException?) {
                    Log.e("main", error.toString())
                }
            })
    }

    private fun handleAccessToken(accessToken: AccessToken?) {
        val authCredential = FacebookAuthProvider.getCredential(accessToken!!.token)
        val firebaseAuth = Firebase.auth
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                activity.startActivity(Intent(activity, MainActivity::class.java))
            } else {
                Toast.makeText(activity, "failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}