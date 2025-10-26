package com.ipt2025.project_dam.ui.device

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailListItemBinding
import com.ipt2025.project_dam.databinding.FragmentSiteDetailsBinding
import com.ipt2025.project_dam.ui.site.SiteDetailUiState
import kotlinx.coroutines.launch

class DeviceDetailsFragment : Fragment() {

    private var _binding: FragmentDeviceDetailListItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DeviceDetailsConnectedDevicesAdapter
    companion object {
        fun newInstance() = DeviceDetailsFragment()
    }

    private val viewModel: DeviceDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DeviceDetailsConnectedDevicesAdapter()
        binding.rvConnectedDevices.adapter = adapter
        binding.rvConnectedDevices.layoutManager = LinearLayoutManager(requireContext())
        val deviceId = arguments?.getString("_id") ?: return

        viewModel.loadDeviceDetails(RetrofitProvider.create(DevicesAPIService::class.java), deviceId)

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
                        binding.devcieType.text = device.type
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            //Suspend function 'suspend fun getDeviceDetails(deviceId: String?): DevicesDetailResponse' can only be called from a coroutine or another suspend function.
            val response = apiService.getDeviceDetails(arguments?.getString("_id"))
        }

        _binding = FragmentDeviceDetailListItemBinding.inflate(inflater, container, false)
        return binding.root
    }
}