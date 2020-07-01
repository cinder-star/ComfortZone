package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sihan.comfortzone.R
import com.sihan.comfortzone.database.DataManager
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.utils.ProductAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var productRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_bar)
        productRecyclerView = view.findViewById(R.id.product_list)
        productRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        activity?.let { ContextCompat.getColor(it, R.color.brand) }?.let {
            swipeRefreshLayout.setColorSchemeColors(
                it
            )
        }
        prepareProductView()
        swipeRefreshLayout.setOnRefreshListener {
            prepareProductView()
        }
        return view
    }

    private fun prepareProductView() {
        swipeRefreshLayout.isRefreshing = true
        val dataManager = DataManager<Product>()
        val productList = dataManager.getItems()
        swipeRefreshLayout.isRefreshing = false
        val productAdapter = activity?.let { ProductAdapter(it, productList) }
        productRecyclerView.adapter = productAdapter
        productAdapter!!.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }
}