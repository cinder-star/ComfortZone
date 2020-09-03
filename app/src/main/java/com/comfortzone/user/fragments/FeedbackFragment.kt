package com.comfortzone.user.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.comfortzone.user.R
import com.comfortzone.user.database.WriteFeedback
import com.comfortzone.user.domains.Feedback
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedbackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedbackFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var customerName: TextInputEditText
    private lateinit var mobileNumber: TextInputEditText
    private lateinit var feedbackText: EditText
    private lateinit var send: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        bindWidgets(view!!)
        bindListeners()
        return view
    }

    @Suppress("SpellCheckingInspection")
    @SuppressLint("SimpleDateFormat")
    private fun bindListeners() {
        send.setOnClickListener {
            if (validate()) {
                val feedback = Feedback(
                    customerName.text.toString(),
                    mobileNumber.text.toString(),
                    feedbackText.text.toString()
                )
                val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                WriteFeedback("feedback", feedback)
                    .write(Firebase.auth.currentUser!!.uid, timeStamp)
                Toast.makeText(activity!!, "Feedback submitted", Toast.LENGTH_SHORT).show()
                activity!!.onBackPressed()
            }
        }
    }

    private fun bindWidgets(view: View) {
        customerName = view.findViewById(R.id.customer_name)
        mobileNumber = view.findViewById(R.id.customer_phone)
        feedbackText = view.findViewById(R.id.opinion)
        send = view.findViewById(R.id.send_opinion)
    }

    private fun validate(): Boolean {
        if (customerName.text.toString().isEmpty()) {
            customerName.error = "Field Cannot be empty"
            customerName.requestFocus()
            return false
        }
        val regex = "(01[356789][0-9]{8})".toRegex()
        val input = mobileNumber.text.toString()
        if (!regex.matches(input)) {
            mobileNumber.error = "Invalid Mobile Number!"
            return false
        }
        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactUsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedbackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}