package com.ipt2025.project_dam.ui.device

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import kotlinx.coroutines.launch

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeviceRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_device_list, container, false)

        // Find the RecyclerView by ID
        recyclerView = view.findViewById(R.id.list)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchDevices()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DeviceRecyclerViewAdapter(emptyList()) { device ->
            val bundle = bundleOf("_id" to device._id)
            findNavController().navigate(R.id.action_deviceFragment_to_deviceDetailsFragment, bundle)
        }
        recyclerView.adapter = adapter
    }

    private fun fetchDevices() {
        Toast.makeText(context, "Fetching devices...", Toast.LENGTH_SHORT).show()

        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getDevices(page = 1, limit = 20)
                Toast.makeText(context, "Found ${response.devices.size} devices", Toast.LENGTH_SHORT).show()

                adapter = DeviceRecyclerViewAdapter(response.devices) { device ->
                    val bundle = bundleOf("_id" to device._id)
                    findNavController().navigate(R.id.action_deviceFragment_to_deviceDetailsFragment, bundle)
                }
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error fetching devices: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}