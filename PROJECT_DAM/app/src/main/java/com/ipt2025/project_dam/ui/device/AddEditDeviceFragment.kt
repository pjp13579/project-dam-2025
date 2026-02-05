package com.ipt2025.project_dam.ui.device

import android.content.Context
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
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceCreateRequest
import com.ipt2025.project_dam.data.api.DeviceUpdateRequest
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentDeviceAddEditBinding
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailsConnectedDeviceCheckboxBinding
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextWatcher
import android.widget.Filter
import java.util.Locale

class AddEditDeviceFragment : Fragment() {

    private var _binding: FragmentDeviceAddEditBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isEditMode = false // same fragment does creation and editing (post & put)
    private var deviceId: String? = null
    private var selectedSiteId: String? = null
    private var sites = listOf<SiteOption>()
    private var allDevices = listOf<DeviceOption>()
    private var displayedDevices = listOf<DeviceOption>() // filtered list after text input
    private var selectedConnectedDevices = mutableSetOf<String>()
    private lateinit var deviceSelectionAdapter: DeviceSelectionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*        binding.etSearchConnected.addTextChangedListener(object : android.text.TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        filterDevices(s.toString())
                    }
                    override fun afterTextChanged(s: android.text.Editable?) {}
                })*/

        // get mode from arguments. Create device or edit device
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            deviceId = it.getString("_id")
        }

        // check permission and navigate back if unauthorized
        if (isEditMode && !RetrofitProvider.canEditDevice() || !isEditMode && !RetrofitProvider.canCreateDevices()) {
            Toast.makeText(context, getString(R.string.accessDenied), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupButton()
        loadSites()
        setupRecyclerView()
        loadAllDevices()
        setupSearchListeners()

        // if edit mode, load existing data
        if (isEditMode && deviceId != null) {
            loadDeviceData(deviceId!!)
        }
    }

    /**
     * Update title based on mode (add or edit)
     */
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

    /**
     * Set up the TextWatcher for the Connected Devices search bar
     */
    private fun setupSearchListeners() {
        binding.etSearchConnected.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterConnectedDevices(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Filter the devices list based on the search query
     */
    private fun filterConnectedDevices(query: String) {
        displayedDevices = if (query.isEmpty()) {
            allDevices
        } else {
            allDevices.filter {
                it.displayName.contains(query, ignoreCase = true)
            }
        }
        deviceSelectionAdapter.updateDevices(displayedDevices)
    }

    /**
     * populate recyclerview input with other devices to select the connected devices
     * checkbox with devices
     */
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

    /**
     * connected devices count
     */
    private fun updateSelectedDeviceCount() {
        binding.tvSelectedDeviceCount.hint = "Selected: ${selectedConnectedDevices.size} device(s)"
    }

    /**
     * load dropdown with sites to select at which site the device is located
     */
    private fun loadSites() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)
                val sitesResponse = apiService.getSites(1, 100)

                sites = sitesResponse.sites.map { site ->
                    SiteOption(site._id, "${site.localName} (${site.type}, ${site.country})")
                }

                val siteNames = sites.map { it.name }

                // Use standard ArrayAdapter (no filtering)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    siteNames
                )

                binding.actvSite.setAdapter(adapter)

                // Handle selection
                binding.actvSite.setOnItemClickListener { _, _, position, _ ->
                    selectedSiteId = sites[position].id
                    binding.actvSite.error = null
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext().applicationContext,
                    "Failed to load sites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * load connected devices
     */
    private fun loadAllDevices() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                val devicesResponse = apiService.getDevices(1, 100)

                val filteredDevices = if (isEditMode && deviceId != null) {
                    devicesResponse.devices.filter { it._id != deviceId }
                } else {
                    devicesResponse.devices
                }

                allDevices = filteredDevices.map { device ->
                    DeviceOption(
                        device._id,
                        "${device.vendor} ${device.type} (${device.serialNumber})"
                    )
                }

                // Initially display all devices
                displayedDevices = allDevices
                deviceSelectionAdapter.updateDevices(displayedDevices)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext().applicationContext, "Failed to load devices", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * isEditMode == true, load existing device information
     */
    private fun loadDeviceData(deviceId: String) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
                val device = apiService.getDeviceDetails(deviceId)

                // populate fields with existing data using Binding
                binding.etVendor.setText(device.vendor)
                binding.etDeviceType.setText(device.type)
                binding.etCategory.setText(device.category)
                binding.etSerialNumber.setText(device.serialNumber)
                binding.etDeviceMacAddress.setText(device.macAddress)
                binding.etState.setText(device.state)

                // find and set the site in dropdown
                val siteName = "${device.site.type} - ${device.site.country}"
                binding.actvSite.setText(siteName)
                selectedSiteId = device.site._id

                // load connected devices
                val existingDeviceIds = device.connectedDevices.map { it._id }.toSet()

                // pass connected devices to the adapter
                // the adapter will reset selectedConnectedDevices, and use the connect devices form above
                deviceSelectionAdapter.setSelectedDevices(existingDeviceIds)

                // update the counter UI
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

    /**
     * validate values in input fields
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        // clear previous errors
        binding.etVendor.error = null
        binding.etDeviceType.error = null
        binding.etCategory.error = null
        binding.etSerialNumber.error = null
        binding.etDeviceMacAddress.error = null
        binding.etState.error = null

        // run validations
        if (binding.etVendor.text.isNullOrEmpty()) {
            binding.etVendor.error = "Required"; isValid = false
        }
        if (binding.etDeviceType.text.isNullOrEmpty()) {
            binding.etDeviceType.error = "Required"; isValid = false
        }
        if (binding.etCategory.text.isNullOrEmpty()) {
            binding.etCategory.error = "Required"; isValid = false
        }
        if (binding.etSerialNumber.text.isNullOrEmpty()) {
            binding.etSerialNumber.error = "Required"; isValid = false
        }
        if (binding.etDeviceMacAddress.text.isNullOrEmpty()) {
            binding.etDeviceMacAddress.error = "Required"; isValid = false
        }
        if (binding.etState.text.isNullOrEmpty()) {
            binding.etState.error = "Required"; isValid = false
        }

        if (selectedSiteId == null) {
            binding.actvSite.error = "Please select a valid site from the list"
            isValid = false
        }

        return isValid
    }

    /**
     * isEditMode == false
     * create need device
     */
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
                val response = apiService.createDevice(deviceRequest)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Device created", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * isEditMode == true
     * update existing device
     */
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
                        Toast.makeText(requireContext(), "Device updated", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // data class for site dropdown options
    data class SiteOption(val id: String, val name: String)


    // data class for connected devices dropdown options
    data class DeviceOption(val id: String, val displayName: String)

    /**
     * custom adapter for site dropdown. implements the search filtering feature
     * starts with type search
     */
    inner class SiteFilterAdapter(
        context: Context,
        resource: Int,
        private val originalList: List<SiteOption>
    ) : ArrayAdapter<SiteOption>(context, resource, originalList) {

        private val filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestions = if (constraint.isNullOrEmpty()) {
                    originalList
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    originalList.filter {
                        it.name.lowercase(Locale.ROOT).contains(filterPattern)
                    }
                }
                results.values = suggestions
                results.count = suggestions.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null && results.count > 0) {
                    addAll(results.values as List<SiteOption>)
                }
                notifyDataSetChanged()
            }
        }

        override fun getFilter(): Filter {
            return filter
        }
    }

    /**
     * device selection adapter
     */
    inner class DeviceSelectionAdapter(
        private var devices: List<DeviceOption>,
        private val selectedDevices: MutableSet<String>,
        private val onCheckChanged: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<DeviceSelectionAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: FragmentDeviceDetailsConnectedDeviceCheckboxBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = FragmentDeviceDetailsConnectedDeviceCheckboxBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val device = devices[position]

            // clear listener to prevent accidental triggering
            holder.binding.checkboxDevice.setOnCheckedChangeListener(null)

            // set matched devices
            holder.binding.checkboxDevice.text = device.displayName
            holder.binding.checkboxDevice.isChecked = selectedDevices.contains(device.id)

            // add back the seach listener
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
    /*
        private fun filterDevices(query: String) {
            val filteredList = if (query.isEmpty()) {
                allDevices
            } else {
                allDevices.filter { it.displayName.contains(query, ignoreCase = true) }
            }
            deviceSelectionAdapter.updateDevices(filteredList)
        }*/
}