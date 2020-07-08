package com.sihan.comfortzone.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.CartItem
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.domains.ShoppingCart
import com.sihan.comfortzone.repositories.MyAdapter
import com.sihan.comfortzone.repositories.OnProductListener

class ProductAdapter(var context: Context, private var products: MutableList<Product> = arrayListOf(), private var onProductListener: OnProductListener):
    MyAdapter, RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_product, null)
        return ViewHolder(view, onProductListener, products)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(products[position], context)
    }


    class ViewHolder(view: View, private var onProductListener: OnProductListener, private var products: MutableList<Product>): RecyclerView.ViewHolder(view), View.OnClickListener{
        fun bindProduct(product: Product, context: Context) {
            val productImage: ImageView = itemView.findViewById(R.id.product_image)
            val imageRef = Firebase.storage.reference.child("products/"+product.imagePath!!)
            itemView.findViewById<TextView>(R.id.product_name).text = product.name
            itemView.findViewById<TextView>(R.id.product_price).text = product.price.toString()
            itemView.findViewById<ImageButton>(R.id.addToCart).setOnClickListener{view ->
                val item = CartItem(product)
                ShoppingCart.addItem(item)
                Snackbar.make(
                    view,
                    "${product.name} added to your cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            itemView.findViewById<ImageButton>(R.id.removeItem).setOnClickListener{view ->
                val item = CartItem(product)
                ShoppingCart.removeItem(item)
                Snackbar.make(
                    view,
                    "${product.name} removed from your cart",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            itemView.setOnClickListener(this)
            GlideApp.with(context)
                .load(imageRef)
                .into(productImage)
        }

        override fun onClick(p0: View?) {
            onProductListener.onProductClicked(products[adapterPosition])
        }
    }

    override fun setItem(items: List<*>) {
        @Suppress("UNCHECKED_CAST")
        products = items as MutableList<Product>
    }

    override fun dataChanged() {
        this.notifyDataSetChanged()
    }
}