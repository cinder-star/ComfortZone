package com.sihan.comfortzone.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.CartItem
import com.sihan.comfortzone.domains.ShoppingCart
import com.sihan.comfortzone.repositories.MyAdapter

class ShoppingCartAdapter(var context: Context, private var cartItems: MutableList<CartItem>, private var textView: TextView) :
    MyAdapter, RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.row_cart_item, parent, false)
        return ViewHolder(layout, textView)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(cartItems[position], context, cartItems)
    }

    class ViewHolder(view: View, private val textView: TextView): RecyclerView.ViewHolder(view) {
        fun bindItem(
            cartItem: CartItem,
            context: Context,
            cartItems: MutableList<CartItem>
        ){
            val productImage: ImageView = itemView.findViewById(R.id.product_image)
            val imageRef = Firebase.storage.reference.child("products/"+cartItem.product.imagePath!!)
            val quantity = itemView.findViewById<TextView>(R.id.product_quantity)
            itemView.findViewById<TextView>(R.id.product_name).text = cartItem.product.name
            quantity.text = cartItem.quantity.toString()
            itemView.findViewById<ImageButton>(R.id.add_one_item).setOnClickListener{
                ShoppingCart.addItem(cartItem)
                quantity.text = cartItem.quantity.toString()
                updateTotalPrice()
            }
            itemView.findViewById<ImageButton>(R.id.minus_one_item).setOnClickListener{
                ShoppingCart.removeItem(cartItem)
                quantity.text = cartItem.quantity.toString()
                if (cartItem.quantity == 0) {
                    cartItems.removeAt(adapterPosition)
                }
                updateTotalPrice()
            }
            GlideApp.with(context)
                .load(imageRef)
                .into(productImage)
        }

        private fun updateTotalPrice() {
            val totalPrice = ShoppingCart.getCart()
                .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
            textView.text = totalPrice.toString()
        }
    }



    override fun setItem(items: List<*>) {
        @Suppress("UNCHECKED_CAST")
        cartItems = items as MutableList<CartItem>
    }

    override fun dataChanged() {
        this.notifyDataSetChanged()
    }
}