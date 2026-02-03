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
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceDetailDataResponse
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailBinding
import kotlinx.coroutines.launch

/**
 * view that displays every information about a device
 */
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
            showErrorMessage(requireContext().getString(R.string.error_device_id))
            setupRetryButton()
            return
        }

        setupUI()
        loadDeviceDetails()
        setupUIBasedOnPermissions()

    }

    /**
     * hide create device button navigation if user doesn't have permission
     */
    private fun setupUIBasedOnPermissions() {
        // hide the edit site button if user doesn't have permission
        if (!RetrofitProvider.canEditDevice()) {
            binding.btnEditDevice.visibility = View.GONE
        } else {
            binding.btnEditDevice.visibility = View.VISIBLE
        }

        if (!RetrofitProvider.canDeleteDevice()) {
            binding.btnDeleteDevice.visibility = View.GONE
        } else {
            binding.btnDeleteDevice.visibility = View.VISIBLE
        }

        if (!RetrofitProvider.canDeleteDevice() && !RetrofitProvider.canEditDevice()) {
            binding.deviceDetailsLinearLayoutHolder.visibility = View.GONE
        }
    }

    /**
     * setup navigation for ui components
     * setup recycler view contents
     */
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
        if (RetrofitProvider.canEditDevice()) {
            binding.btnEditDevice.setOnClickListener {
                navigateToEditDevice()
            }
        }

        // Setup delete button
        if (RetrofitProvider.canDeleteDevice()) {
            binding.btnDeleteDevice.setOnClickListener {
                showDeleteConfirmationDialog()
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = DeviceDetailsConnectedDevicesAdapter()
        binding.rvConnectedDevices.adapter = adapter
        binding.rvConnectedDevices.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * set metadata required by add/edit device  fragment and set navigation
     */
    private fun navigateToEditDevice() {
        // isEditMode distinguish between add and edit. same fragment do add and edit.
        deviceId?.let { id ->
            val bundle = Bundle().apply {
                putString("_id", id)
                putBoolean("isEditMode", true)
            }
            findNavController().navigate(
                R.id.action_deviceDetailsFragment_to_addEditDeviceFragment,
                bundle
            )
        }
    }

    /**
     * confirmation pop-up for soft deleting device
     */
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.alert_delete_device))
            .setMessage(requireContext().getString(R.string.alert_delete_device_description))
            .setPositiveButton(requireContext().getString(R.string.delete)) { _, _ ->
                deleteDevice()
            }
            .setNegativeButton(requireContext().getString(R.string.cancel), null)
            .show()
    }

    /**
     * soft delete device
     */
    private fun deleteDevice() {
        if (!RetrofitProvider.canDeleteDevice()) {
            Toast.makeText(context, "Access denied. Can't delete device", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            try {

                deviceId?.let { id ->
                    val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                    val response = apiService.deleteDevice(id)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.success_delete_device),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.fail_delete_device),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * given a device id, request detailed information
     */
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

    /**
     * when editing a device, data fields are populated with the existing device information
     * this function populates the text fields with the existing device information
     */
    private fun displayDeviceData(device: DeviceDetailDataResponse) {
        // Device information
        val ctx = requireContext()
        binding.vendor.text = ctx.getString(R.string.label_vendor).replace("{value}", device.vendor)
        binding.category.text =
            ctx.getString(R.string.label_category).replace("{value}", device.category)
        binding.deviceTypeInfo.text =
            ctx.getString(R.string.label_type).replace("{value}", device.type)
        binding.serialNumber.text =
            ctx.getString(R.string.label_serial_number).replace("{value}", device.serialNumber)
        binding.MacAddress.text =
            ctx.getString(R.string.label_mac_address).replace("{value}", device.macAddress)
        binding.state.text = ctx.getString(R.string.label_state).replace("{value}", device.state)

        // Site information
        binding.siteType.text =
            ctx.getString(R.string.label_site_type).replace("{value}", device.site.type)
        binding.siteCountry.text =
            ctx.getString(R.string.label_country).replace("{value}", device.site.country)
        binding.siteCity.text =
            ctx.getString(R.string.label_city).replace("{value}", device.site.address.city)
        binding.siteState.text =
            ctx.getString(R.string.label_state).replace("{value}", device.site.address.state)
        binding.siteStreet.text =
            ctx.getString(R.string.label_street).replace("{value}", device.site.address.street)
        binding.siteZipcode.text =
            ctx.getString(R.string.label_zip_code).replace("{value}", device.site.address.zipCode)

        // Connected devices
        adapter.setConnectedDevices(device.connectedDevices)

        // Show connected devices section only if there are any
        if (device.connectedDevices.isNotEmpty()) {
            binding.connectedDevicesSection.visibility = View.VISIBLE
        } else {
            binding.connectedDevicesSection.visibility = View.GONE
        }
    }

    /**
     * show loading spinner while device details is being received
     */
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