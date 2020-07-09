package com.sihan.comfortzone.fragments

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
import com.sihan.comfortzone.R
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.domains.Product
import com.sihan.comfortzone.repositories.OnProductListener
import com.sihan.comfortzone.utils.SearchProductAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), OnProductListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var searchResult: RecyclerView
    private lateinit var stack: MyStack<String>
    private lateinit var searchAdapter: SearchProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        bindWidgets(view!!)
        return view
    }

    private fun bindWidgets(view: View) {
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>
        searchResult = view.findViewById(R.id.search_result)
        searchResult.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }

    fun prepareSearchResult(search: String) {
        if (search.isNotEmpty()) {
            val query =
                Firebase.database.reference.child("/products").orderByChild("name").startAt(search)
                    .endAt(search + "\uf8ff")
            val options = FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product::class.java)
                .build()
            searchAdapter = SearchProductAdapter(activity!!, options, this)
            searchResult.adapter = searchAdapter
            searchAdapter.startListening()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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
}