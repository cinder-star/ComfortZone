package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R
import com.sihan.comfortzone.database.DataManager
import com.sihan.comfortzone.domains.Category
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.repositories.OnCategoryListener
import com.sihan.comfortzone.repositories.OnProductListener
import com.sihan.comfortzone.utils.CategoryAdapter
import com.sihan.comfortzone.utils.ProductAdapter
import io.paperdb.Paper

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
        val dataManager = DataManager("/categories")
        val categoryList: MutableList<Category> = mutableListOf()
        val categoryAdapter = activity?.let { CategoryAdapter(it, categoryList, this) }
        categoryRecyclerView.adapter = categoryAdapter
        dataManager.setListener<Category>(categoryAdapter!!)
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
        bundle.putSerializable("stack", stack)
        if (category.subCategory == "yes") {
            loadFragment(SubCategoryFragment(), bundle)
        } else{
            loadFragment(CategoryProductFragment(), bundle)
        }
    }
}