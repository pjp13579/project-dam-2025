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
import com.google.android.material.button.MaterialButton
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.components.EndlessScrollListener
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import kotlinx.coroutines.launch

class DeviceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeviceRecyclerViewAdapter

    private lateinit var btn_add_device : MaterialButton

    private var currentPage = 1
    private val PAGE_LIMIT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflate the layout
        val view = inflater.inflate(R.layout.fragment_device_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        btn_add_device = view.findViewById(R.id.btn_add_device)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        fetchDevices(currentPage, PAGE_LIMIT)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DeviceRecyclerViewAdapter(mutableListOf()) { device ->
            val bundle = bundleOf("_id" to device._id)
            findNavController().navigate(R.id.action_deviceFragment_to_deviceDetailsFragment, bundle)
        }

        recyclerView.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchDevices(currentPage, PAGE_LIMIT)
            }
        })

        recyclerView.adapter = adapter
    }

    private fun fetchDevices(page: Int, limit: Int) {
        //Toast.makeText(context, "Fetching devices...", Toast.LENGTH_SHORT).show()

        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getDevices(page = page, limit = limit)
                //Toast.makeText(context, "Found ${response.devices.size} devices", Toast.LENGTH_SHORT).show()

                adapter.addDevices(response.devices)

                recyclerView.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error fetching devices: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun setupClickListeners() {
        btn_add_device.setOnClickListener {

            findNavController().navigate(R.id.action_deviceFragment_to_addEditDeviceFragment)
        }
    }


}