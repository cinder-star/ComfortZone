package com.comfortzone.user.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.signature.ObjectKey
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.comfortzone.user.R
import com.comfortzone.user.domains.Category
import com.comfortzone.user.repositories.OnCategoryListener

class CategoryAdapter(
    var context: Context,
    options: FirebaseRecyclerOptions<Category>,
    private var onCategoryListener: OnCategoryListener
) :
    FirebaseRecyclerAdapter<Category, CategoryAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.row_categoty, parent, false)
        return ViewHolder(layout, onCategoryListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Category) {
        holder.bindItem(model, context)
    }

    class ViewHolder(
        view: View,
        private var onCategoryListener: OnCategoryListener
    ) : RecyclerView.ViewHolder(view) {
        fun bindItem(category: Category, context: Context) {
            val imageView: ImageView = itemView.findViewById(R.id.category_image)
            val imageRef = Firebase.storage.reference.child("categories/" + category.imagePath)
            itemView.findViewById<TextView>(R.id.category_name).text = category.name
            itemView.setOnClickListener {
                onCategoryListener.onCategoryClicked(category)
            }
            GlideApp.with(context)
                .load(imageRef)
                .signature(ObjectKey(category.imagePath+category.lastModified))
                .into(imageView)
        }
    }
}