package com.ipt2025.project_dam.ui.site

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.components.EndlessScrollListener
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentSiteListBinding
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * list of existing sites
 */
class SiteFragment : Fragment() {

    private var _binding: FragmentSiteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SiteRecyclerViewAdapter
    private var currentPage = 1
    private val PAGE_LIMIT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check permission and navigate back if unauthorized
        if (!RetrofitProvider.canViewSites()) {
            Toast.makeText(context, getString(R.string.accessDenied), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupRecyclerView()
        setupClickListeners()
        setupUIBasedOnPermissions()
        fetchSites(currentPage, PAGE_LIMIT)
    }

    /**
     * check permission to add sites and hide navigation button if unauthorized
     */
    private fun setupUIBasedOnPermissions() {
        // hide the add site button if user doesn't have permission
        if (!RetrofitProvider.canCreateSite()) {
            binding.btnAddSite.visibility = View.GONE
        } else {
            binding.btnAddSite.visibility = View.VISIBLE
        }
    }

    /**
     * setup recycler view data and item navigation
     */
    private fun setupRecyclerView() {

        // set bundle. persist clicked item id to then perform getBtId/ getDetails API request
        adapter = SiteRecyclerViewAdapter(mutableListOf()) { site ->
            val bundle = Bundle().apply {
                putString("_id", site._id)
            }
            findNavController().navigate(R.id.action_siteFragment_to_siteDetailsFragment, bundle)
        }

        binding.list.layoutManager = LinearLayoutManager(context)

        // setup scroll listener to request next page when reaching end of records
        binding.list.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchSites(currentPage, PAGE_LIMIT)
            }
        })
        binding.list.adapter = adapter
    }

    /**
     * get page of existing sites
     */
    private fun fetchSites(page: Int, limit: Int) {
        val apiService = RetrofitProvider.create(SitesAPIService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getSites(page = page, limit = limit)
                adapter.addSites(response.sites)
            } catch (e: CancellationException) { } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * set navigation listener to add a site
     */
    private fun setupClickListeners() {
        binding.btnAddSite.setOnClickListener {
            // validate permission before navigating
            if (RetrofitProvider.canCreateSite()) {
                findNavController().navigate(R.id.action_siteFragment_to_addEditSiteFragment)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.accessDenied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}