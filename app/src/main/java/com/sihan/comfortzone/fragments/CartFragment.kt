package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.domains.ShoppingCart
import com.sihan.comfortzone.utils.ShoppingCartAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: ShoppingCartAdapter
    private lateinit var totalPriceView: TextView
    private lateinit var checkout: Button
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
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        bindWidgets(view)
        setupCartView(view)
        calculatePrice(view)
        return view
    }

    private fun bindWidgets(view: View?) {
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>
        totalPriceView = view!!.findViewById(R.id.total_price)
        checkout = view.findViewById(R.id.checkout)
        checkout.setOnClickListener {
            stack.push("orderFragment")
            val bundle = Bundle()
            bundle.putSerializable("stack", stack)
            loadFragment(InfoOrderFragment(), bundle)
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

    private fun calculatePrice(view: View?) {
        val totalPrice: Double
        totalPrice = ShoppingCart.getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
        view!!.findViewById<TextView>(R.id.total_price).text = totalPrice.toString()
    }

    private fun setupCartView(view: View?) {
        cartAdapter = activity?.let { ShoppingCartAdapter(it, ShoppingCart.getCart(), totalPriceView) }!!
        cartAdapter.notifyDataSetChanged()
        cartRecyclerView = view!!.findViewById(R.id.shopping_cart_recyclerView)
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}