package com.ipt2025.project_dam.ui.site

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.data.api.SitesRequest
import com.ipt2025.project_dam.ui.site.placeholder.PlaceholderContent
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class SiteFragment : Fragment() {

    private var columnCount = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SiteRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_site_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {

            recyclerView = view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = SiteRecyclerViewAdapter(emptyList(), {})
                fetchSites()
            }
        }
        return view
    }

    private fun fetchSites(){
        val apiService = RetrofitProvider.create(SitesAPIService::class.java)

        lifecycleScope.launch {
            try{
                val response = apiService.getSites(page = 1, limit = 20)

                val appContext = context?.applicationContext
                adapter = SiteRecyclerViewAdapter(response.sites, {
                    val bundle = bundleOf("_id" to it._id)
                    findNavController().navigate(R.id.action_siteFragment_to_siteDetailsFragment, bundle)
                })
                recyclerView.adapter = adapter
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SiteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}