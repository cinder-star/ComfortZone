package com.sihan.comfortzone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.sihan.comfortzone.R
import com.sihan.comfortzone.repositories.EmailAuthActivity
import com.sihan.comfortzone.repositories.EmailLogin

class SignUpActivity : EmailAuthActivity(R.layout.activity_signup) {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var button: Button

    override fun bindWidgets() {
        username = findViewById(R.id.credential)
        password = findViewById(R.id.password)
        button = findViewById(R.id.log_in)
    }

    override fun bindListeners() {
        button.setOnClickListener {
            if (validate()) {
                signUpUser(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun validate(): Boolean {
        val regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()
        val input = username.text.toString()
        if (!regex.matches(input)) {
            username.error = "Invalid email!"
            username.requestFocus()
            return false
        }
        return true
    }
}