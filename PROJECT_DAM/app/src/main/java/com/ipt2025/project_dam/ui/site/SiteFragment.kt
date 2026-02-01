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
            Toast.makeText(context, "Access denied", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupRecyclerView()
        setupClickListeners()
        setupUIBasedOnPermissions()
        fetchSites(currentPage, PAGE_LIMIT)
    }

    private fun setupUIBasedOnPermissions() {
        // hide the add site button if user doesn't have permission
        if (!RetrofitProvider.canCreateSite()) {
            binding.btnAddSite.visibility = View.GONE
        } else {
            binding.btnAddSite.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        // Initialize adapter with click handler
        adapter = SiteRecyclerViewAdapter(mutableListOf()) { site ->
            val bundle = Bundle().apply {
                putString("_id", site._id)
            }
            findNavController().navigate(R.id.action_siteFragment_to_siteDetailsFragment, bundle)
        }

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchSites(currentPage, PAGE_LIMIT)
            }
        })
        binding.list.adapter = adapter
    }

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

    private fun setupClickListeners() {
        binding.btnAddSite.setOnClickListener {
            // validate permission before navigating
            if (RetrofitProvider.canCreateSite()) {
                findNavController().navigate(R.id.action_siteFragment_to_addEditSiteFragment)
            } else {
                Toast.makeText(
                    context,
                    "You don't have permission to create sites",
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