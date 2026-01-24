package com.ipt2025.project_dam.ui.site

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SiteDeviceDataResponse
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentDeviceListItemBinding
import com.ipt2025.project_dam.databinding.FragmentSiteDetailsBinding
import kotlinx.coroutines.launch

class SiteDetailsFragment : Fragment() {
    private var _binding: FragmentSiteDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SiteDetailsViewModel by viewModels()

    // We'll use this for our inline adapter
    private var devices = listOf<SiteDeviceDataResponse>()
    private var siteId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get siteId from arguments
        siteId = arguments?.getString("_id") ?: return

        // setup RecyclerView with inline adapter
        setupDeviceList()

        // load site details
        loadSiteDetails()

        // setup button listeners
        setupClickListeners()
    }

    private fun setupDeviceList() {
        // INLINE ADAPTER: Defined right here in the fragment
        val deviceAdapter = object : RecyclerView.Adapter<DeviceViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
                // Inflate using the binding class for the list item
                val binding = FragmentDeviceListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DeviceViewHolder(binding)
            }

            override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
                val device = devices[position]

                // Set device data to views using binding
                holder.binding.deviceType.text = device.type
                holder.binding.deviceSerialNumber.text = device.serialNumber
                holder.binding.deviceState.text = device.state

                holder.itemView.setOnClickListener {
                    val bundle = Bundle().apply {
                        putString("_id", device._id)
                    }
                    findNavController().navigate(R.id.action_siteDetailsFragment_to_deviceDetailsFragment, bundle)
                }
            }

            override fun getItemCount(): Int = devices.size
        }

        binding.rvDevices.layoutManager = LinearLayoutManager(context)
        binding.rvDevices.adapter = deviceAdapter
    }

    // ViewHolder now accepts the Binding object
    private class DeviceViewHolder(val binding: FragmentDeviceListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private fun loadSiteDetails() {
        val apiService = RetrofitProvider.create(SitesAPIService::class.java)
        siteId?.let { id ->
            viewModel.loadSiteDetails(apiService, id)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is SiteDetailUiState.Loading -> {
                        binding.siteDetailName.text = requireContext().getString(R.string.loading)
                    }
                    is SiteDetailUiState.Success -> {
                        val site = state.site
                        binding.siteDetailName.text = site.localName
                        binding.siteDetailType.text = site.type
                        binding.siteDetailCountry.text = site.country

                        // update device list
                        site.devicesAtSite?.let { deviceList ->
                            devices = deviceList
                            binding.rvDevices.adapter?.notifyDataSetChanged()

                            // Optional: Show/hide empty state
                            if (deviceList.isEmpty()) {
                                showEmptyDevicesState()
                            }
                        } ?: run {
                            showEmptyDevicesState()
                        }
                    }
                    is SiteDetailUiState.Error -> {
                        binding.siteDetailName.text = "Error: ${state.message}"
                    }
                }
            }
        }
    }

    private fun showEmptyDevicesState() {
        println("No devices at this site")
    }

    private fun setupClickListeners() {
        binding.btnEditSite.setOnClickListener {
            siteId?.let { id ->
                val bundle = Bundle().apply {
                    putString("_id", id)
                    putBoolean("isEditMode", true)
                }
                findNavController().navigate(R.id.action_siteDetailsFragment_to_addEditSiteFragment, bundle)
            }
        }

        binding.btnDeleteSite.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.delete_site))
            .setMessage(requireContext().getString(R.string.delete_site_label))
            .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                deleteSite()
            }
            .setNegativeButton(requireContext().getString(R.string.cancel), null)
            .show()
    }

    private fun deleteSite() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)
                siteId?.let { id ->
                    apiService.deleteSite(id)
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to delete site: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}