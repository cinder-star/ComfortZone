package com.sihan.comfortzone.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sihan.comfortzone.R
import com.sihan.comfortzone.activities.MainActivity
import com.sihan.comfortzone.domains.CartItem
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.domains.ShoppingCart

class ProductAdapter(var context: Context, private var products: List<Product> = arrayListOf()):
        RecyclerView.Adapter<ProductAdapter.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_product, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindProduct(product: Product) {
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = product.price.toString()
            itemView.findViewById<ImageButton>(R.id.addToCart).setOnClickListener{view ->
                val item = CartItem(product)
                ShoppingCart.addItem(item)
                Snackbar.make(
                    view,
                    "${product.name} added to your cart",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            itemView.findViewById<ImageButton>(R.id.removeItem).setOnClickListener{view ->
                val item = CartItem(product)
                ShoppingCart.removeItem(item)
                Snackbar.make(
                    view,
                    "${product.name} removed from your cart",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}