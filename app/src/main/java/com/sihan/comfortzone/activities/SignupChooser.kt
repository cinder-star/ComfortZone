package com.sihan.comfortzone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R

class SignupChooser : AppCompatActivity() {
    private lateinit var email_sign_up: Button
    private lateinit var facebook_login_button: LoginButton
    private lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_chooser)
        title = "লগইন"
        bindWidgets()
        bindListeners()
    }

    private fun bindListeners() {
        email_sign_up.setOnClickListener {
            startActivity(Intent(this@SignupChooser, SignupActivity::class.java))
        }
    }

    private fun bindWidgets() {
        email_sign_up = findViewById(R.id.email_sign_up)
        facebook_login_button = findViewById(R.id.facebook_login_button)
        facebook_login_button.setPermissions("email", "public_profile")
        callbackManager = CallbackManager.Factory.create()
        facebook_login_button.registerCallback(
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
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
            this
        ) { p0 ->
            if (p0.isSuccessful) {
                startActivity(Intent(this@SignupChooser, MainActivity::class.java))
            } else {
                Toast.makeText(this@SignupChooser, "failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}