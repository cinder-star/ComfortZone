package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.domains.ShoppingCart
import com.sihan.comfortzone.utils.GlideApp
import kotlin.math.max

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SingleProductViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SingleProductViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var plusButton: ImageButton
    private lateinit var minusButton: ImageButton
    private lateinit var removeButton: ImageButton
    private lateinit var addToCartButton: Button
    private lateinit var product: Product
    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var totalPrice: TextView
    private lateinit var quantity: TextView
    private lateinit var imageRef: StorageReference
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
        val view = inflater.inflate(R.layout.fragment_single_product_view, container, false)
        bindWidgets(view!!)
        bindListeners()
        return view
    }

    private fun bindWidgets(view: View) {
        plusButton = view.findViewById(R.id.add_one_item)
        minusButton = view.findViewById(R.id.minus_one_item)
        removeButton = view.findViewById(R.id.remove_item)
        quantity = view.findViewById(R.id.product_quantity)
        productName = view.findViewById(R.id.product_name)
        productPrice = view.findViewById(R.id.product_price)
        productImage = view.findViewById(R.id.product_image)
        totalPrice = view.findViewById(R.id.total_price)
        addToCartButton = view.findViewById(R.id.add_to_cart)
        product = this.arguments!!.getSerializable("product") as Product
        imageRef = Firebase.storage.reference.child("products/"+product.imagePath)
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>
    }

    private fun bindListeners() {
        quantity.text = "0"
        productName.text = product.name!!
        productPrice.text = product.price.toString()
        totalPrice.text = "0"
        GlideApp.with(activity!!)
            .load(imageRef)
            .into(productImage)
        plusButton.setOnClickListener {
            quantity.text = (quantity.text.toString().toInt() + 1).toString()
            updatePrice()
        }
        minusButton.setOnClickListener {
            quantity.text = max(quantity.text.toString().toInt() - 1, 0).toString()
            updatePrice()
        }
        addToCartButton.setOnClickListener {
            val total = quantity.text.toString().toInt()
            if (total != 0) {
                ShoppingCart.bulkAdd(product, quantity.text.toString().toInt())
                activity!!.onBackPressed()
            }
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

    private fun updatePrice() {
        totalPrice.text = (quantity.text.toString().toInt() * product.price!!).toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SingleProductViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SingleProductViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}