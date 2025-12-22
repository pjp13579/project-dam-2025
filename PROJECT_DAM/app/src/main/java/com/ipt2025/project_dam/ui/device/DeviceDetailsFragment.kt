package com.ipt2025.project_dam.ui.device

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceDetailDataResponse
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailBinding
import kotlinx.coroutines.launch

class DeviceDetailsFragment : Fragment() {

    private var _binding: FragmentDeviceDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DeviceDetailsConnectedDevicesAdapter
    private val viewModel: DeviceDetailsViewModel by viewModels()
    private var deviceId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get deviceId from arguments
        deviceId = arguments?.getString("_id")

        if (deviceId == null || deviceId.isNullOrEmpty()) {
            showErrorMessage("No device ID provided")
            setupRetryButton()
            return
        }

        setupUI()
        loadDeviceDetails()
    }

    private fun setupUI() {
        // Setup back button in toolbar
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_deviceDetailsFragment_to_dashboard)
        }

        // Setup retry button
        binding.retryButton.setOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = DeviceDetailsConnectedDevicesAdapter()
        binding.rvConnectedDevices.adapter = adapter
        binding.rvConnectedDevices.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadDeviceDetails() {
        deviceId?.let { id ->
            // Show loading state
            showLoading(true)
            binding.errorContainer.visibility = View.GONE

            val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
            viewModel.loadDeviceDetails(apiService, id)

            lifecycleScope.launch {
                viewModel.uiState.collect { state ->
                    showLoading(false)

                    when (state) {
                        is DeviceDetailsUiState.Loading -> {
                            showLoading(true)
                        }
                        is DeviceDetailsUiState.Success -> {
                            val device = state.device
                            displayDeviceData(device)
                            binding.scrollView.visibility = View.VISIBLE
                            binding.errorContainer.visibility = View.GONE
                        }
                        is DeviceDetailsUiState.Error -> {
                            showErrorMessage("Error: ${state.message}")
                            binding.scrollView.visibility = View.GONE
                            binding.errorContainer.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun displayDeviceData(device: DeviceDetailDataResponse) {
        // Device information
        binding.vendor.text = "Vendor: ${device.vendor}"
        binding.category.text = "Category: ${device.category}"
        binding.deviceTypeInfo.text = "Type: ${device.type}"
        binding.serialNumber.text = "Serial Number: ${device.serialNumber}"
        binding.MacAddress.text = "MAC Address: ${device.macAddress}"
        binding.state.text = "State: ${device.state}"

        // Site information
        binding.siteType.text = "Site Type: ${device.site.type}"
        binding.siteCountry.text = "Country: ${device.site.country}"
        binding.siteCity.text = "City: ${device.site.address.city}"
        binding.siteState.text = "State: ${device.site.address.state}"
        binding.siteStreet.text = "Street: ${device.site.address.street}"
        binding.siteZipcode.text = "Zip Code: ${device.site.address.zipCode}"

        // Connected devices
        adapter.setConnectedDevices(device.connectedDevices)

        // Show connected devices section only if there are any
        if (device.connectedDevices.isNotEmpty()) {
            binding.connectedDevicesSection.visibility = View.VISIBLE
        } else {
            binding.connectedDevicesSection.visibility = View.GONE
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            binding.scrollView.visibility = View.GONE
        }
    }

    private fun showErrorMessage(message: String) {
        binding.errorText.text = message
        binding.errorContainer.visibility = View.VISIBLE
    }

    private fun setupRetryButton() {
        binding.retryButton.visibility = View.VISIBLE
        binding.retryButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}