package com.comfortzone.user.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.comfortzone.user.R
import com.comfortzone.user.domains.MyStack
import com.comfortzone.user.domains.Order
import com.comfortzone.user.domains.OrderItem
import com.comfortzone.user.domains.ShoppingCart
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var orderButton: Button
    private lateinit var customerName: TextView
    private lateinit var customerNumber: TextView
    private lateinit var customerAddresses: TextView
    private lateinit var confirmationHolder: RelativeLayout
    private lateinit var stack: MyStack<String>

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
        val view = inflater.inflate(R.layout.fragment_info_order, container, false)
        bindWidgets(view!!)
        bindListeners(view)
        return view
    }

    private fun bindListeners(view: View) {
        orderButton.setOnClickListener {
            uploadInformation(view)
        }
    }

    private fun uploadInformation(view: View) {
        if (isInternetAvailable()) {
            if (validate()) {
                totalOrderProcess(view)
            }
        } else {
            Toast.makeText(activity!!, "No network connection!", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun totalOrderProcess(view: View) {
        view.hideKeyboard()
        @Suppress("SpellCheckingInspection")
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val price = calculatePrice()
        val order = Order(
            timeStamp,
            Firebase.auth.currentUser!!.uid,
            price,
            customerName.text.toString(),
            customerAddresses.text.toString(),
            customerNumber.text.toString(),
            "no"
        )
        val database = Firebase.database.reference
        database.child("/order/$timeStamp").setValue(order)
            .addOnSuccessListener {
                uploadOrder(timeStamp)
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity!!,
                    "Unexpected error occurred!\n Order could not be sent.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun loadFragment(fragment: Fragment, bundle: Bundle) {
        // load fragment
        fragment.arguments = bundle
        val manager = activity!!.supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_holder, fragment)
        manager.addToBackStack(null)
        manager.commit()
    }

    private fun calculatePrice(): Double {
        return ShoppingCart.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
    }

    private fun uploadOrder(orderId: String) {
        val cartItems = ShoppingCart.getCart()
        val orderItems: MutableList<OrderItem> = mutableListOf()
        cartItems.forEach {
            orderItems.add(
                OrderItem(
                    it.product.id,
                    it.product.name,
                    it.product.imagePath,
                    it.product.price,
                    it.quantity,
                    it.product.lastModified
                )
            )
        }
        orderItems.forEach {
            Firebase.database.reference.child("/orderItems/" + orderId + "/" + it.id).setValue(it)
        }
        ShoppingCart.clearCart()
        stack.clear()
        stack.push("cartFragment")
        val bundle = Bundle()
        bundle.putSerializable("stack", stack)
        confirmationHolder.visibility = View.VISIBLE
        Handler(Looper.myLooper()!!).postDelayed({
            loadFragment(CartFragment(), bundle)
        }, 1000)
    }

    private fun bindWidgets(view: View) {
        orderButton = view.findViewById(R.id.send_order)
        customerName = view.findViewById(R.id.customer_name)
        customerAddresses = view.findViewById(R.id.customer_address)
        customerNumber = view.findViewById(R.id.customer_phone)
        confirmationHolder = view.findViewById(R.id.confirmation_holder)
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>
    }

    private fun validate(): Boolean {
        if (customerName.text.toString().isEmpty()) {
            customerName.error = "Field Cannot be empty"
            customerName.requestFocus()
            return false
        }
        if (customerAddresses.text.toString().isEmpty()) {
            customerAddresses.error = "Field Cannot be empty"
            customerAddresses.requestFocus()
            return false
        }
        val regex = "(01[356789][0-9]{8})".toRegex()
        val input = customerNumber.text.toString()
        if (!regex.matches(input)) {
            customerNumber.error = "Invalid Mobile Number!"
            return false
        }
        return true
    }

    private fun View.hideKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}