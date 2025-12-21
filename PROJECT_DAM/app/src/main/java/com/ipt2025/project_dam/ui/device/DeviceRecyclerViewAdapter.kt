package com.ipt2025.project_dam.ui.device

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ipt2025.project_dam.data.api.DeviceDataResponse
import com.ipt2025.project_dam.databinding.FragmentDeviceListItemBinding

class DeviceRecyclerViewAdapter(
    private val devices: MutableList<DeviceDataResponse>,
    private val onClick: (DeviceDataResponse) -> Unit
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentDeviceListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size

    fun addDevices(newDevices: List<DeviceDataResponse>) {
        val start = devices.size
        devices.addAll(newDevices)
        notifyItemRangeInserted(start, newDevices.size)
    }

    inner class ViewHolder(
        private val binding: FragmentDeviceListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: DeviceDataResponse) {
            binding.deviceType.text = device.type
            binding.deviceSerialNumber.text = device.serialNumber
            binding.deviceState.text = device.state

            binding.root.setOnClickListener {
                onClick(device)
            }
        }
    }
}
