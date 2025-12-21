package com.ipt2025.project_dam.ui.site

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.components.EndlessScrollListener
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import kotlinx.coroutines.launch

class SiteFragment : Fragment() {

    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: SiteRecyclerViewAdapter
    private lateinit var btnAddSite: MaterialButton

    private var currentPage = 1
    private val PAGE_LIMIT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // INFLATE THE NEW LAYOUT (CHANGE THIS LINE)
        return inflater.inflate(R.layout.fragment_site_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views HERE (not in onCreateView)
        recyclerView = view.findViewById(R.id.list)
        btnAddSite = view.findViewById(R.id.btn_add_site)

        setupRecyclerView()
        fetchSites(currentPage, PAGE_LIMIT)
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        // Initialize adapter with click handler
        adapter = SiteRecyclerViewAdapter(mutableListOf()) { site ->
            // Navigate to site details when clicked
            val bundle = Bundle().apply {
                putString("_id", site._id)
            }
            findNavController().navigate(R.id.action_siteFragment_to_siteDetailsFragment, bundle)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchSites(currentPage, PAGE_LIMIT)
            }
        })
        recyclerView.adapter = adapter
    }

    private fun fetchSites(page : Int, limit : Int){
        val apiService = RetrofitProvider.create(SitesAPIService::class.java)

        lifecycleScope.launch {
            try{
                val response = apiService.getSites(page = page, limit = limit)
                adapter.addSites(response.sites)
            }catch (e: Exception) {
                e.printStackTrace()
                // Show error message
            }
        }
    }

    private fun setupClickListeners() {
        btnAddSite.setOnClickListener {
            // We'll create this action later
            findNavController().navigate(R.id.action_siteFragment_to_addEditSiteFragment)
        }
    }
}