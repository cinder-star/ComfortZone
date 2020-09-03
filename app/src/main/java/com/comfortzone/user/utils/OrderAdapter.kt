package com.comfortzone.user.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comfortzone.user.R
import com.comfortzone.user.domains.Order
import com.comfortzone.user.repositories.OnOrderListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class OrderAdapter(
    private val context: Context,
    private val onOrderListener: OnOrderListener,
    options: FirebaseRecyclerOptions<Order>
) :
    FirebaseRecyclerAdapter<Order, OrderAdapter.ViewHolder>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return ViewHolder(view, onOrderListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Order) {
        holder.bindView(model)
    }

    class ViewHolder(itemView: View, private val onOrderListener: OnOrderListener) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView(order: Order) {
            itemView.setOnClickListener {
                onOrderListener.onOrderClick(order)
            }
        }
    }
}