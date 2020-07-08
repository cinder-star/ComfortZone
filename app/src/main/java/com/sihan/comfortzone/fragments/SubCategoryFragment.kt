package com.sihan.comfortzone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sihan.comfortzone.R
import com.sihan.comfortzone.database.DataManager
import com.sihan.comfortzone.domains.Category
import com.sihan.comfortzone.domains.MyStack
import com.sihan.comfortzone.repositories.OnCategoryListener
import com.sihan.comfortzone.utils.CategoryAdapter
import io.paperdb.Paper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubCategoryFragment : Fragment(), OnCategoryListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var subCategoryRecyclerView: RecyclerView
    private lateinit var subCategoryName: TextView
    private lateinit var stack: MyStack<String>

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
        val view = inflater.inflate(R.layout.fragment_sub_category, container, false)
        bindWidgets(view!!)
        prepareSubcategory()
        return view
    }

    private fun prepareSubcategory() {
        val dataManager = DataManager("/subCategories/"+stack.peek())
        val subCategoryList: MutableList<Category> = mutableListOf()
        val subCategoryAdapter = activity?.let { CategoryAdapter(it, subCategoryList, this) }
        subCategoryRecyclerView.adapter = subCategoryAdapter
        dataManager.setListener<Category>(subCategoryAdapter!!)
    }

    private fun bindWidgets(view: View) {
        @Suppress("UNCHECKED_CAST")
        stack = this.arguments!!.getSerializable("stack") as MyStack<String>
        subCategoryRecyclerView = view.findViewById(R.id.sub_category)
        subCategoryRecyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        subCategoryName = view.findViewById(R.id.sub_category_name)
        subCategoryName.text = stack.peek()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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