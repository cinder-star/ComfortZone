package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sihan.comfortzone.R
import com.sihan.comfortzone.database.DataManager
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.repositories.OnProductListener
import com.sihan.comfortzone.utils.ProductAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment(), OnProductListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var productRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_bar)

        productRecyclerView = view.findViewById(R.id.product_list)
        productRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        activity?.let { ContextCompat.getColor(it, R.color.brand) }?.let {
            swipeRefreshLayout.setColorSchemeColors(
                it
            )
        }
        prepareProductView()
        return view
    }

    private fun prepareProductView() {
        val dataManager = DataManager("/products/foods")
        val productList: ArrayList<Product> = arrayListOf()
        val productAdapter = activity?.let { ProductAdapter(it, productList, this) }
        productRecyclerView.adapter = productAdapter
        dataManager.setListener<Product>(productAdapter!!)
    }

    companion object {
        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }

    override fun onProductClicked(position: Int) {
        loadFragment(SingleProductViewFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val manager = activity!!.supportFragmentManager.beginTransaction()
        manager.replace(R.id.fragment_holder, fragment)
        manager.addToBackStack(null)
        manager.commit()
    }
}