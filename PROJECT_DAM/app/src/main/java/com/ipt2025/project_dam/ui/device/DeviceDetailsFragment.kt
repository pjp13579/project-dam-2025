package com.ipt2025.project_dam.ui.device

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
import com.google.android.material.button.MaterialButton
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
    private lateinit var btnEditDevice: MaterialButton
    private lateinit var btnDeleteDevice: MaterialButton

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

        // Initialize buttons
        btnEditDevice = view.findViewById(R.id.btn_edit_device)
        btnDeleteDevice = view.findViewById(R.id.btn_delete_device)

        setupUI()
        loadDeviceDetails()
    }

    private fun setupUI() {
        // Setup back button in toolbar
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup retry button
        binding.retryButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Setup edit button
        btnEditDevice.setOnClickListener {
            navigateToEditDevice()
        }

        // Setup delete button
        btnDeleteDevice.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = DeviceDetailsConnectedDevicesAdapter()
        binding.rvConnectedDevices.adapter = adapter
        binding.rvConnectedDevices.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun navigateToEditDevice() {
        deviceId?.let { id ->
            val bundle = Bundle().apply {
                putString("_id", id)
                putBoolean("isEditMode", true)
            }
            findNavController().navigate(R.id.action_deviceDetailsFragment_to_addEditDeviceFragment, bundle)
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Device")
            .setMessage("Are you sure you want to delete this device?")
            .setPositiveButton("Delete") { _, _ ->
                deleteDevice()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteDevice() {
        lifecycleScope.launch {
            try {
                deviceId?.let { id ->
                    val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                    val response = apiService.deleteDevice(id)

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Device deleted successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete device", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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