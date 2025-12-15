package com.ipt2025.project_dam.ui.device

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailBinding // CORRECT BINDING!
import kotlinx.coroutines.launch

class DeviceDetailsFragment : Fragment() {

    // Use FragmentDeviceDetailBinding (from fragment_device_detail.xml)
    private var _binding: FragmentDeviceDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DeviceDetailsConnectedDevicesAdapter
    private val viewModel: DeviceDetailsViewModel by viewModels()
    private var deviceId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the CORRECT layout
        _binding = FragmentDeviceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get deviceId from arguments
        deviceId = arguments?.getString("_id")

        // DEBUG: Check if we got the ID
        println("DeviceDetailsFragment - Received deviceId: $deviceId")

        if (deviceId == null) {
            // Show error
            binding.vendor.text = "Error: No device ID provided"
            return
        }

        setupRecyclerView()
        loadDeviceDetails()
    }

    private fun setupRecyclerView() {
        adapter = DeviceDetailsConnectedDevicesAdapter()
        binding.rvConnectedDevices.adapter = adapter
        binding.rvConnectedDevices.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadDeviceDetails() {
        deviceId?.let { id ->
            val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
            viewModel.loadDeviceDetails(apiService, id)

            lifecycleScope.launch {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is DeviceDetailsUiState.Loading -> {
                            binding.vendor.text = "Loading..."
                        }
                        is DeviceDetailsUiState.Success -> {
                            val device = state.device
                            binding.vendor.text = device.vendor
                            binding.category.text = device.category
                            binding.deviceTypeInfo.text = device.type // FIX TYPO: was "devcieType"
                            binding.siteType.text = device.site.type
                            binding.siteCountry.text = device.site.country
                            binding.siteCity.text = device.site.address.city
                            binding.siteState.text = device.site.address.state
                            binding.siteStreet.text = device.site.address.street
                            binding.siteZipcode.text = device.site.address.zipCode
                            binding.MacAddress.text = device.macAddress
                            binding.state.text = device.state
                            binding.serialNumber.text = device.serialNumber

                            adapter.setConnectedDevices(device.connectedDevices)
                        }
                        is DeviceDetailsUiState.Error -> {
                            binding.vendor.text = "Error: ${state.message}"
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}