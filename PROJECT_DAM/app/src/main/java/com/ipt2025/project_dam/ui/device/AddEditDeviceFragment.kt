package com.ipt2025.project_dam.ui.device

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceCreateRequest
import com.ipt2025.project_dam.data.api.DeviceUpdateRequest
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import kotlinx.coroutines.launch

class AddEditDeviceFragment : Fragment() {

    private lateinit var etVendor: TextInputEditText
    private lateinit var etType: TextInputEditText
    private lateinit var etCategory: TextInputEditText
    private lateinit var etSerialNumber: TextInputEditText
    private lateinit var etMacAddress: TextInputEditText
    private lateinit var etState: TextInputEditText
    private lateinit var actvSite: AutoCompleteTextView
    private lateinit var etSearchConnected: TextInputEditText
    private lateinit var rvDeviceSelection: RecyclerView
    private lateinit var tvSelectedDeviceCount: TextInputLayout
    private lateinit var btnSave: MaterialButton

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
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get mode from arguments. Create device or edit device
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            deviceId = it.getString("_id")
        }

        // get UI components
        etVendor = view.findViewById(R.id.et_vendor)
        etType = view.findViewById(R.id.et_device_type)
        etSerialNumber = view.findViewById(R.id.et_serial_number)
        etMacAddress = view.findViewById(R.id.et_device_mac_address)
        etState = view.findViewById(R.id.et_state)
        actvSite = view.findViewById(R.id.actv_site)
        etSearchConnected = view.findViewById(R.id.et_search_connected)
        rvDeviceSelection = view.findViewById(R.id.rv_device_selection)
        tvSelectedDeviceCount = view.findViewById(R.id.tv_selected_device_count)
        btnSave = view.findViewById(R.id.btn_save)


        val categoryLayout = view.findViewById<TextInputLayout?>(R.id.et_category_layout)
        if (categoryLayout != null && categoryLayout.editText != null) {
            etCategory = categoryLayout.editText as TextInputEditText
        } else {
            etCategory = view.findViewById(R.id.et_category) ?: TextInputEditText(requireContext())
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
        btnSave.text = if (isEditMode) "Update Device" else "Create Device"

        btnSave.setOnClickListener {
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

        rvDeviceSelection.layoutManager = LinearLayoutManager(requireContext())
        rvDeviceSelection.adapter = deviceSelectionAdapter
    }

    private fun updateSelectedDeviceCount() {
        tvSelectedDeviceCount.hint = "Selected: ${selectedConnectedDevices.size} device(s)"
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
                actvSite.setAdapter(adapter)

                // select the site ID if selected on the sites dropdown
                actvSite.setOnItemClickListener { _, _, position, _ ->
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
                //in practice, a device can connect to itself but fucckk this project already im done with this already
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

                // Populate fields with existing data
                etVendor.setText(device.vendor)
                etType.setText(device.type)
                etCategory.setText(device.category)
                etSerialNumber.setText(device.serialNumber)
                etMacAddress.setText(device.macAddress)
                etState.setText(device.state)

                // Find and set the site in dropdown
                val siteName = "${device.site.type} - ${device.site.country}"
                actvSite.setText(siteName)
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
        listOf(etVendor, etType, etCategory, etSerialNumber, etMacAddress, etState).forEach {
            it.error = null
        }

        // Required fields validation
        if (etVendor.text.isNullOrEmpty()) {
            etVendor.error = "Vendor is required"
            isValid = false
        }

        if (etType.text.isNullOrEmpty()) {
            etType.error = "Type is required"
            isValid = false
        }

        if (etCategory.text.isNullOrEmpty()) {
            etCategory.error = "Category is required"
            isValid = false
        }

        if (etSerialNumber.text.isNullOrEmpty()) {
            etSerialNumber.error = "Serial number is required"
            isValid = false
        }

        if (etMacAddress.text.isNullOrEmpty()) {
            etMacAddress.error = "MAC address is required"
            isValid = false
        }

        if (etState.text.isNullOrEmpty()) {
            etState.error = "State is required"
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
                    vendor = etVendor.text.toString(),
                    category = etCategory.text.toString(),
                    type = etType.text.toString(),
                    serialNumber = etSerialNumber.text.toString(),
                    macAddress = etMacAddress.text.toString(),
                    state = etState.text.toString(),
                    site = selectedSiteId!!,  // Send site ID as string
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
                    println("DEBUG - API Error ${response.code()}: $errorBody")
                    Toast.makeText(
                        requireContext(),
                        "Failed to create device (${response.code()}): ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("DEBUG - Exception: ${e.message}")
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
                        vendor = etVendor.text.toString(),
                        category = etCategory.text.toString(),
                        type = etType.text.toString(),
                        serialNumber = etSerialNumber.text.toString(),
                        macAddress = etMacAddress.text.toString(),
                        state = etState.text.toString(),
                        site = selectedSiteId,  // Send site ID as string
                        connectedDevices = selectedConnectedDevices.toList(),
                        isActive = true
                    )

                    println("DEBUG - Updating device $id with request: $deviceRequest")

                    val response = apiService.updateDevice(id, deviceRequest)

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Device updated successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        println("DEBUG - API Error ${response.code()}: $errorBody")
                        Toast.makeText(
                            requireContext(),
                            "Failed to update device (${response.code()}): ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("DEBUG - Exception: ${e.message}")
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // data classes for dropdown options
    data class SiteOption(val id: String, val name: String)
    data class DeviceOption(val id: String, val displayName: String)

    // recyclerView adapter for device selection
    inner class DeviceSelectionAdapter(
        private var devices: List<DeviceOption>,
        private val selectedDevices: MutableSet<String>,
        private val onCheckChanged: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<DeviceSelectionAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val checkBox: MaterialCheckBox = itemView.findViewById(R.id.checkbox_device)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_device_checkbox, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val device = devices[position]
            holder.checkBox.text = device.displayName
            holder.checkBox.isChecked = selectedDevices.contains(device.id)

            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
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