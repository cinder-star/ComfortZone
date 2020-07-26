package com.comfortzone.user.activities

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.comfortzone.user.R
import com.comfortzone.user.repositories.EmailAuthActivity

class LoginActivity : EmailAuthActivity(R.layout.activity_log_in) {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var doSignUp: TextView
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    override fun bindWidgets() {
        username = findViewById(R.id.credential_value)
        password = findViewById(R.id.password_value)
        button = findViewById(R.id.log_in)
        doSignUp = findViewById(R.id.redirect)
        progressBar = findViewById(R.id.progress_bar)
    }

    override fun bindListeners() {
        button.setOnClickListener {
            button.hideKeyboard()
            if (validate()) {
                progressBar.visibility = View.VISIBLE
                logInUser(username.text.toString(), password.text.toString(), progressBar)
            }
        }
        doSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun validate(): Boolean {
        val regex =
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])".toRegex()
        val input = username.text.toString()
        if (!regex.matches(input)) {
            username.error = "Invalid email!"
            username.requestFocus()
            return false
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Password field empty!"
            password.requestFocus()
            return false
        }
        return true
    }

    private fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}