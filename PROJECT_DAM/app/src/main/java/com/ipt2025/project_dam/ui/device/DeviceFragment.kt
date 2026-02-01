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
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.components.EndlessScrollListener
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentDeviceListBinding
import kotlinx.coroutines.launch

class DeviceFragment : Fragment() {

    private var _binding: FragmentDeviceListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DeviceRecyclerViewAdapter
    private var currentPage = 1
    private val PAGE_LIMIT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check permission and navigate back if unauthorized
        if (!RetrofitProvider.canViewDevices()) {
            Toast.makeText(context, "Access denied", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }


        setupRecyclerView()
        setupClickListeners()
        setupUIBasedOnPermissions()
        fetchDevices(currentPage, PAGE_LIMIT)
    }

    private fun setupUIBasedOnPermissions() {
        // hide the add device button if user doesn't have permission
        if (!RetrofitProvider.canCreateDevices()) {
            binding.btnAddDevice.visibility = View.GONE
        } else {
            binding.btnAddDevice.visibility = View.VISIBLE
        }
    }


    private fun setupRecyclerView() {
        binding.list.layoutManager = LinearLayoutManager(context)
        adapter = DeviceRecyclerViewAdapter(mutableListOf()) { device ->
            val bundle = bundleOf("_id" to device._id)
            findNavController().navigate(R.id.action_deviceFragment_to_deviceDetailsFragment, bundle)
        }

        binding.list.addOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore(page: Int) {
                currentPage++
                fetchDevices(currentPage, PAGE_LIMIT)
            }
        })

        binding.list.adapter = adapter
    }

    private fun fetchDevices(page: Int, limit: Int) {
        val apiService = RetrofitProvider.create(DevicesAPIService::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getDevices(page = page, limit = limit)
                adapter.addDevices(response.devices)

                // If you are re-assigning adapter (usually not recommended, but keeping your logic):
                binding.list.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error fetching devices: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnAddDevice.setOnClickListener {
            // validate permission before navigating
            if (RetrofitProvider.canCreateDevices()) {
                findNavController().navigate(R.id.action_deviceFragment_to_addEditDeviceFragment)
            } else {
                Toast.makeText(context, "You don't have permission to create devices", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}