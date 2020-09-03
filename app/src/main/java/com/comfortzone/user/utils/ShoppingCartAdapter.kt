package com.comfortzone.user.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.comfortzone.user.R
import com.comfortzone.user.domains.CartItem
import com.comfortzone.user.domains.ShoppingCart
import com.comfortzone.user.repositories.MyAdapter

class ShoppingCartAdapter(
    var context: Context,
    private var cartItems: MutableList<CartItem>,
    private var textView: TextView
) :
    MyAdapter, RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.row_cart_item, parent, false)
        return ViewHolder(layout, textView)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(cartItems[position], context, cartItems, this)
    }

    class ViewHolder(view: View, private val textView: TextView) : RecyclerView.ViewHolder(view) {
        private val totalPriceView: TextView = itemView.findViewById(R.id.total_price)
        fun bindItem(
            cartItem: CartItem,
            context: Context,
            cartItems: MutableList<CartItem>,
            shoppingCartAdapter: ShoppingCartAdapter
        ) {
            val productImage: ImageView = itemView.findViewById(R.id.product_image)
            val imageRef =
                Firebase.storage.reference.child("products/" + cartItem.product.imagePath!!)
            val quantity = itemView.findViewById<TextView>(R.id.product_quantity)
            itemView.findViewById<TextView>(R.id.product_name).text = cartItem.product.name
            quantity.text = cartItem.quantity.toString()
            totalPriceView.text = (cartItem.quantity * cartItem.product.price!!).toString()
            itemView.findViewById<ImageButton>(R.id.add_one_item).setOnClickListener {
                ShoppingCart.addItem(cartItem)
                quantity.text = cartItem.quantity.toString()
                updateTotalPrice()
            }
            itemView.findViewById<ImageButton>(R.id.minus_one_item).setOnClickListener {
                ShoppingCart.removeItem(cartItem)
                quantity.text = cartItem.quantity.toString()
                if (cartItem.quantity == 0) {
                    cartItems.removeAt(adapterPosition)
                    shoppingCartAdapter.notifyDataSetChanged()
                }
                updateTotalPrice()
            }
            itemView.findViewById<ImageButton>(R.id.remove_item).setOnClickListener {
                ShoppingCart.completelyRemoveItem(cartItem)
                cartItems.removeAt(adapterPosition)
                shoppingCartAdapter.notifyDataSetChanged()
                updateTotalPrice()
            }
            GlideApp.with(context)
                .load(imageRef)
                .signature(ObjectKey(cartItem.product.imagePath+cartItem.product.lastModified))
                .into(productImage)
        }

        private fun updateTotalPrice() {
            val totalPrice = ShoppingCart.getCart()
                .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }
            textView.text = totalPrice.toString()
            totalPriceView.text = totalPrice.toString()
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