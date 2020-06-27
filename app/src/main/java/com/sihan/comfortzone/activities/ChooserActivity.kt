package com.sihan.comfortzone.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.sihan.comfortzone.R
import com.sihan.comfortzone.repositories.FacebookLogin

class ChooserActivity : AppCompatActivity() {
    private lateinit var emailSignUp: Button
    private lateinit var phoneNumberSignUp: Button
    private lateinit var facebookLoginButton: LoginButton
    private lateinit var facebookLogin: FacebookLogin
    private lateinit var callbackManager: CallbackManager
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
    }

    private fun bindWidgets() {
        emailSignUp = findViewById(R.id.email_sign_up)
        facebookLoginButton = findViewById(R.id.facebook_login_button)
        facebookLoginButton.setPermissions("email", "public_profile")
        callbackManager = CallbackManager.Factory.create()
        facebookLogin = FacebookLogin(this, facebookLoginButton, callbackManager)
        facebookLogin.setup()
        phoneNumberSignUp = findViewById(R.id.phone_number_sign_up)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}