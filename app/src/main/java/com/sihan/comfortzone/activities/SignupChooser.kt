package com.sihan.comfortzone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sihan.comfortzone.R

class SignupChooser : AppCompatActivity() {
    private lateinit var email_sign_up: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_chooser)
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
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}