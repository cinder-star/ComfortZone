package com.comfortzone.user.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.comfortzone.user.R
import com.comfortzone.user.domains.Category
import com.comfortzone.user.domains.MyStack
import com.comfortzone.user.domains.Product
import com.comfortzone.user.repositories.OnCategoryListener
import com.comfortzone.user.repositories.OnProductListener
import com.comfortzone.user.utils.CategoryAdapter
import com.comfortzone.user.utils.ProductAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment(), OnProductListener, OnCategoryListener {
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var stack: MyStack<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        bindWidgets(view)
        prepareProductView()
        prepareCategoryView()
        return view
    }

    private fun bindWidgets(view: View) {
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>

        productRecyclerView = view.findViewById(R.id.product_list)
        productRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        categoryRecyclerView = view.findViewById(R.id.category_list)
        categoryRecyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun prepareCategoryView() {
        val ref =
            Firebase.database.reference.child("/categories")
        val query = ref.orderByChild("parent").equalTo("no")
        ref.keepSynced(true)
        val options = FirebaseRecyclerOptions.Builder<Category>()
            .setQuery(query, Category::class.java)
            .build()
        val categoryAdapter = CategoryAdapter(activity!!, options, this)
        categoryRecyclerView.adapter = categoryAdapter
        categoryAdapter.startListening()
    }

    private fun prepareProductView() {
        val ref = Firebase.database.reference.child("/products")
        ref.keepSynced(true)
        val query = ref.orderByChild("popular").equalTo("yes")
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()
        val productAdapter = ProductAdapter(activity!!, options, this)
        productRecyclerView.adapter = productAdapter
        productAdapter.startListening()
    }

    companion object {
        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }

    override fun onProductClicked(product: Product) {
        val bundle = Bundle()
        stack.push("singleProductFragment")
        bundle.putSerializable("product", product)
        bundle.putSerializable("stack", stack)
        loadFragment(SingleProductViewFragment(), bundle)
    }

    private fun loadFragment(fragment: Fragment, bundle: Bundle) {
        // load fragment
        fragment.arguments = bundle
        val manager = activity!!.supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_holder, fragment)
        manager.addToBackStack(null)
        manager.commit()
    }

    override fun onCategoryClicked(category: Category) {
        val bundle = Bundle()
        stack.push(category.name!!)
        val id = category.id!!
        bundle.putSerializable("stack", stack)
        bundle.putSerializable("id", id)
        if (category.subCategory == "yes") {
            loadFragment(SubCategoryFragment(), bundle)
        } else {
            loadFragment(CategoryProductFragment(), bundle)
        }
    }
}