package com.sihan.comfortzone.utils

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.CartItem
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.domains.ShoppingCart
import com.sihan.comfortzone.repositories.OnProductListener

class SearchProductAdapter(
    private val context: Context,
    options: FirebaseRecyclerOptions<Product>,
    private val onProductListener: OnProductListener
) :
    FirebaseRecyclerAdapter<Product, SearchProductAdapter.ViewHolder>(options) {
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
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = product.price.toString()
            itemView.findViewById<TextView>(R.id.old_product_price).text = product.price.toString()
            itemView.findViewById<TextView>(R.id.old_product_price).paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG
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
                .into(productImage)
        }

    }
}