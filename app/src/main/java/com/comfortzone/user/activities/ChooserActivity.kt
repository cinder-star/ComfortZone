package com.comfortzone.user.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.comfortzone.user.R
import com.comfortzone.user.repositories.FacebookLogin

class ChooserActivity : AppCompatActivity() {
    private lateinit var emailSignUp: Button
    private lateinit var phoneNumberSignUp: Button
    private lateinit var facebookLoginButton: LoginButton
    private lateinit var facebookLogin: FacebookLogin
    private lateinit var callbackManager: CallbackManager
    private lateinit var redirect: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)
        title = "লগইন"
        bindWidgets()
        bindListeners()
    }

    private fun bindListeners() {
        emailSignUp.setOnClickListener {
            startActivity(Intent(this@ChooserActivity, LoginActivity::class.java))
        }
        phoneNumberSignUp.setOnClickListener {
            startActivity(Intent(this@ChooserActivity, PhoneLoginActivity::class.java))
        }
        redirect.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun bindWidgets() {
        emailSignUp = findViewById(R.id.email_sign_up)
        facebookLoginButton = findViewById(R.id.facebook_login_button)
        facebookLoginButton.setPermissions("email", "public_profile")
        callbackManager = CallbackManager.Factory.create()
        facebookLogin = FacebookLogin(this, facebookLoginButton, callbackManager)
        facebookLogin.setup()
        phoneNumberSignUp = findViewById(R.id.phone_number_sign_up)
        redirect = findViewById(R.id.redirect)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}