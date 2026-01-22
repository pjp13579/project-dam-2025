package com.ipt2025.project_dam.ui.device

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceCreateRequest
import com.ipt2025.project_dam.data.api.DeviceUpdateRequest
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentAddEditDeviceBinding
import com.ipt2025.project_dam.databinding.ItemDeviceCheckboxBinding
import kotlinx.coroutines.launch

class AddEditDeviceFragment : Fragment() {

    private var _binding: FragmentAddEditDeviceBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isEditMode = false
    private var deviceId: String? = null
    private var selectedSiteId: String? = null
    private var sites = listOf<SiteOption>()
    private var allDevices = listOf<DeviceOption>()
    private var selectedConnectedDevices = mutableSetOf<String>()
    private lateinit var deviceSelectionAdapter: DeviceSelectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get mode from arguments. Create device or edit device
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            deviceId = it.getString("_id")
        }

        setupButton()
        loadSites()
        setupRecyclerView()
        loadAllDevices()

        // if edit mode, load existing data
        if (isEditMode && deviceId != null) {
            loadDeviceData(deviceId!!)
        }
    }

    private fun setupButton() {
        binding.btnSave.text = if (isEditMode) "Update Device" else "Create Device"

        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                if (isEditMode) {
                    updateDevice()
                } else {
                    createDevice()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        deviceSelectionAdapter = DeviceSelectionAdapter(
            emptyList(),
            selectedConnectedDevices
        ) { deviceId, isChecked ->
            if (isChecked) {
                selectedConnectedDevices.add(deviceId)
            } else {
                selectedConnectedDevices.remove(deviceId)
            }
            updateSelectedDeviceCount()
        }

        binding.rvDeviceSelection.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDeviceSelection.adapter = deviceSelectionAdapter
    }

    private fun updateSelectedDeviceCount() {
        binding.tvSelectedDeviceCount.hint = "Selected: ${selectedConnectedDevices.size} device(s)"
    }

    private fun loadSites() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)
                val sitesResponse = apiService.getSites(1, 100)

                sites = sitesResponse.sites.map { site ->
                    SiteOption(site._id, "${site.localName} (${site.type}, ${site.country})")
                }

                val siteNames = sites.map { it.name }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    siteNames
                )
                binding.actvSite.setAdapter(adapter)

                // select the site ID if selected on the sites dropdown
                binding.actvSite.setOnItemClickListener { _, _, position, _ ->
                    selectedSiteId = sites[position].id
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Failed to load sites: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadAllDevices() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                val devicesResponse = apiService.getDevices(1, 100)

                // filter out current device if in edit mode.
                val filteredDevices = if (isEditMode && deviceId != null) {
                    devicesResponse.devices.filter { it._id != deviceId }
                } else {
                    devicesResponse.devices
                }

                allDevices = filteredDevices.map { device ->
                    DeviceOption(device._id, "${device.vendor} ${device.type} (${device.serialNumber})")
                }

                // add devices to the list
                deviceSelectionAdapter.updateDevices(allDevices)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Failed to load devices",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadDeviceData(deviceId: String) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                val device = apiService.getDeviceDetails(deviceId)

                // Populate fields with existing data using Binding
                binding.etVendor.setText(device.vendor)
                binding.etDeviceType.setText(device.type)
                binding.etCategory.setText(device.category)
                binding.etSerialNumber.setText(device.serialNumber)
                binding.etDeviceMacAddress.setText(device.macAddress)
                binding.etState.setText(device.state)

                // Find and set the site in dropdown
                val siteName = "${device.site.type} - ${device.site.country}"
                binding.actvSite.setText(siteName)
                selectedSiteId = device.site._id

                // Load connected devices
                device.connectedDevices.forEach { connectedDevice ->
                    selectedConnectedDevices.add(connectedDevice._id)
                }

                // Update device selection list
                deviceSelectionAdapter.setSelectedDevices(selectedConnectedDevices)
                updateSelectedDeviceCount()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Failed to load device data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Clear previous errors
        binding.etVendor.error = null
        binding.etDeviceType.error = null
        binding.etCategory.error = null
        binding.etSerialNumber.error = null
        binding.etDeviceMacAddress.error = null
        binding.etState.error = null

        // Required fields validation
        if (binding.etVendor.text.isNullOrEmpty()) {
            binding.etVendor.error = "Vendor is required"
            isValid = false
        }

        if (binding.etDeviceType.text.isNullOrEmpty()) {
            binding.etDeviceType.error = "Type is required"
            isValid = false
        }

        if (binding.etCategory.text.isNullOrEmpty()) {
            binding.etCategory.error = "Category is required"
            isValid = false
        }

        if (binding.etSerialNumber.text.isNullOrEmpty()) {
            binding.etSerialNumber.error = "Serial number is required"
            isValid = false
        }

        if (binding.etDeviceMacAddress.text.isNullOrEmpty()) {
            binding.etDeviceMacAddress.error = "MAC address is required"
            isValid = false
        }

        if (binding.etState.text.isNullOrEmpty()) {
            binding.etState.error = "State is required"
            isValid = false
        }

        if (selectedSiteId == null) {
            Toast.makeText(requireContext(), "Please select a site", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun createDevice() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)

                val deviceRequest = DeviceCreateRequest(
                    vendor = binding.etVendor.text.toString(),
                    category = binding.etCategory.text.toString(),
                    type = binding.etDeviceType.text.toString(),
                    serialNumber = binding.etSerialNumber.text.toString(),
                    macAddress = binding.etDeviceMacAddress.text.toString(),
                    state = binding.etState.text.toString(),
                    site = selectedSiteId!!,
                    connectedDevices = selectedConnectedDevices.toList(),
                    isActive = true
                )

                Log.d("deviceRequest create", Gson().toJson(deviceRequest))

                val response = apiService.createDevice(deviceRequest)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Device created successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(
                        requireContext(),
                        "Failed to create device (${response.code()}): ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateDevice() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                deviceId?.let { id ->
                    val deviceRequest = DeviceUpdateRequest(
                        vendor = binding.etVendor.text.toString(),
                        category = binding.etCategory.text.toString(),
                        type = binding.etDeviceType.text.toString(),
                        serialNumber = binding.etSerialNumber.text.toString(),
                        macAddress = binding.etDeviceMacAddress.text.toString(),
                        state = binding.etState.text.toString(),
                        site = selectedSiteId,
                        connectedDevices = selectedConnectedDevices.toList(),
                        isActive = true
                    )

                    val response = apiService.updateDevice(id, deviceRequest)

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Device updated successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update device (${response.code()}): ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // data classes for dropdown options
    data class SiteOption(val id: String, val name: String)
    data class DeviceOption(val id: String, val displayName: String)

    // recyclerView adapter for device selection with Binding support
    inner class DeviceSelectionAdapter(
        private var devices: List<DeviceOption>,
        private val selectedDevices: MutableSet<String>,
        private val onCheckChanged: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<DeviceSelectionAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemDeviceCheckboxBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemDeviceCheckboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val device = devices[position]
            holder.binding.checkboxDevice.text = device.displayName
            holder.binding.checkboxDevice.isChecked = selectedDevices.contains(device.id)

            holder.binding.checkboxDevice.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(device.id, isChecked)
            }
        }

        override fun getItemCount(): Int = devices.size

        fun updateDevices(newDevices: List<DeviceOption>) {
            devices = newDevices
            notifyDataSetChanged()
        }

        fun setSelectedDevices(deviceIds: Set<String>) {
            selectedDevices.clear()
            selectedDevices.addAll(deviceIds)
            notifyDataSetChanged()
        }
    }
}