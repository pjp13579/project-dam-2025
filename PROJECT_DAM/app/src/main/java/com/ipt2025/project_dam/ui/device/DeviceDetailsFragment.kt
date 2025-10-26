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
import kotlinx.coroutines.launch

class DeviceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DeviceDetailsFragment()
    }

    private val viewModel: DeviceDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
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

        val view = inflater.inflate(R.layout.fragment_device_details, container, false)

        return view;
    }
}