package com.ipt2025.project_dam.ui.site

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentLoginBinding
import com.ipt2025.project_dam.databinding.FragmentSiteDetailsBinding

class SiteDetailsFragment : Fragment() {
    private var _binding: FragmentSiteDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SiteDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val siteId = arguments?.getString("_id") ?: return
        viewModel.loadSiteDetails(RetrofitProvider.create(SitesAPIService::class.java), siteId)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is SiteDetailUiState.Loading -> {
                        binding.siteDetailName.text = "Loading..."
                    }
                    is SiteDetailUiState.Success -> {
                        val site = state.site
                        binding.siteDetailName.text = site.localName
                        binding.siteDetailType.text = site.type
                        binding.siteDetailCountry.text = site.country
                    }
                    is SiteDetailUiState.Error -> {
                        binding.siteDetailName.text = "Error: ${state.message}"
                    }
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}