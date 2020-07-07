package com.sihan.comfortzone.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.Category
import com.sihan.comfortzone.repositories.MyAdapter

class CategoryAdapter(var context: Context, private var categories: List<Category>) :
    MyAdapter, RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.row_categoty, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(categories[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(category: Category) {
            itemView.findViewById<TextView>(R.id.category_name).text = category.name
        }
    }

    override fun setItem(items: List<*>) {
        @Suppress("UNCHECKED_CAST")
        categories = items as List<Category>
    }

    override fun dataChanged() {
        this.notifyDataSetChanged()
    }
}