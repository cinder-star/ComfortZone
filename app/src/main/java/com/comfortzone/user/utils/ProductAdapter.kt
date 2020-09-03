package com.comfortzone.user.utils

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.signature.ObjectKey
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.comfortzone.user.R
import com.comfortzone.user.domains.CartItem
import com.comfortzone.user.domains.Product
import com.comfortzone.user.domains.ShoppingCart
import com.comfortzone.user.repositories.OnProductListener

class ProductAdapter(
    private val context: Context,
    options: FirebaseRecyclerOptions<Product>,
    private val onProductListener: OnProductListener
) :
    FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false)
        return ViewHolder(view, context, onProductListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {
        holder.bindView(model)
    }

    class ViewHolder(
        itemView: View,
        private val context: Context,
        private val onProductListener: OnProductListener
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(product: Product) {
            val productImage: ImageView = itemView.findViewById(R.id.product_image)
            val imageRef = Firebase.storage.reference.child("products/" + product.imagePath!!)
            val prevPrice: TextView = itemView.findViewById(R.id.old_product_price)
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = product.price.toString()
            prevPrice.text = product.price.toString()
            if (product.special == "yes") {
                itemView.findViewById<TextView>(R.id.offer).visibility = View.VISIBLE
                itemView.findViewById<LinearLayout>(R.id.old_price_holder).visibility = View.VISIBLE
                prevPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                prevPrice.text = product.prevPrice.toString()
            }
            itemView.findViewById<ImageButton>(R.id.addToCart).setOnClickListener { view ->
                val item = CartItem(product)
                ShoppingCart.addItem(item)
                Snackbar.make(
                    view,
                    "${product.name} added to your cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            itemView.setOnClickListener {
                onProductListener.onProductClicked(product)
            }

            GlideApp.with(context)
                .load(imageRef)
                .signature(ObjectKey(product.imagePath+product.lastModified))
                .into(productImage)
        }

    }
}