package com.sihan.comfortzone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.sihan.comfortzone.R

class PhoneLoginActivity : AppCompatActivity() {
    private lateinit var mobileNoField: EditText
    private lateinit var confirmMobileNo: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login)
        bindWidgets()
        bindListeners()
    }

    private fun bindWidgets() {
        mobileNoField = findViewById(R.id.mobile_no)
        confirmMobileNo = findViewById(R.id.confirm_mobile_no)
    }

    private fun bindListeners() {
        confirmMobileNo.setOnClickListener {
            val i = Intent(this@PhoneLoginActivity, ConfirmPhoneNumber::class.java)
            i.putExtra("mobile_no", mobileNoField.text.toString())
            if (validate()) {
                startActivity(i)
            }
        }
    }

    private fun validate(): Boolean {
        val regex = "(01[356789][0-9]{8})".toRegex()
        val input = mobileNoField.text.toString()
        if (!regex.matches(input)) {
            mobileNoField.error = "Invalid Mobile Number!"
            return false
        }
        return true
    }
}