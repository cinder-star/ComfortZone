package com.sihan.comfortzone.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R
import java.util.concurrent.TimeUnit

class ConfirmPhoneNumber : AppCompatActivity() {
    private lateinit var code: EditText
    private lateinit var confirmCode: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var systemVerificationCode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_phone_number)
        bindWidgets()
        checkMobileNumber()
        bindListeners()
    }

    private fun checkMobileNumber() {
        val mobileNumber = intent.getStringExtra("mobile_no")
        if (mobileNumber != null) {
            sendVerificationCodeToUser(mobileNumber)
        } else {
            Toast.makeText(this, "No phone numbers provided", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerificationCodeToUser(mobileNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+88$mobileNumber",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks
        )
    }

    private fun bindWidgets() {
        code = findViewById(R.id.code_value)
        confirmCode = findViewById(R.id.confirm_code)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                systemVerificationCode = p0
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val verificationCode = p0.smsCode
                if (verificationCode != null) {
                    progressBar.visibility = View.VISIBLE
                    verifyCode(verificationCode)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@ConfirmPhoneNumber, p0.message, Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun verifyCode(verificationCode: String) {
        val phoneAuthCredential =
            PhoneAuthProvider.getCredential(systemVerificationCode, verificationCode)
        signInUserByCredential(phoneAuthCredential)
    }

    private fun signInUserByCredential(phoneAuthCredential: PhoneAuthCredential) {
        val auth = Firebase.auth
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val i = Intent(this@ConfirmPhoneNumber, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            } else {
                Toast.makeText(this@ConfirmPhoneNumber, task.exception?.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun bindListeners() {
        confirmCode.setOnClickListener {
            if (validate()) {
                val userCode = code.text.toString()
                progressBar.visibility = View.VISIBLE
                verifyCode(userCode)
            }
        }
    }

    private fun validate(): Boolean {
        val regex = "([1-9][0-9]{5})".toRegex()
        val input = code.text.toString()
        if (!regex.matches(input)) {
            code.error = "Invalid Code!"
            code.requestFocus()
            return false
        }
        return true
    }
}