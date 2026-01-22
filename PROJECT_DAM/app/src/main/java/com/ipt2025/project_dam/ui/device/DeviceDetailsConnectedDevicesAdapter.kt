package com.ipt2025.project_dam.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.data.api.DeviceDetailsConnectedDeviceResponse
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailListItemConnectedDeviceItemBinding

class DeviceDetailsConnectedDevicesAdapter : RecyclerView.Adapter<DeviceDetailsConnectedDevicesAdapter.ConnectedDeviceViewHolder>()  {

    private var connectedDevices: List<DeviceDetailsConnectedDeviceResponse> = emptyList()

    // ViewHolder now takes the binding instead of a raw View
    inner class ConnectedDeviceViewHolder(val binding: FragmentDeviceDetailListItemConnectedDeviceItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedDeviceViewHolder {
        val binding = FragmentDeviceDetailListItemConnectedDeviceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConnectedDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectedDeviceViewHolder, position: Int) {
        val device = connectedDevices[position]

        // Access views directly through the binding property
        holder.binding.rvcategory.text = "Category: ${device.category}"
        holder.binding.rvtype.text = "Type: ${device.type}"
        holder.binding.rvserialNumber.text = "Serial: ${device.serialNumber}"
        holder.binding.rvmacAddress.text = "MAC: ${device.macAddress}"
        holder.binding.rvsitetype.text = "Site Type: ${device.site.type}"
        holder.binding.rvcountry.text = "Country: ${device.site.country}"
        holder.binding.rvstreet.text = "Street: ${device.site.address.street}"
    }

    override fun getItemCount(): Int = connectedDevices.size

    fun setConnectedDevices(devices: List<DeviceDetailsConnectedDeviceResponse>) {
        this.connectedDevices = devices
        notifyDataSetChanged()
    }
}