package com.ipt2025.project_dam.ui.device

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.DeviceDetailsConnectedDeviceResponse

class DeviceDetailsConnectedDevicesAdapter : RecyclerView.Adapter<DeviceDetailsConnectedDevicesAdapter.ConnectedDeviceViewHolder>()  {

    private var connectedDevices: List<DeviceDetailsConnectedDeviceResponse> = emptyList()

    inner class ConnectedDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category: TextView = itemView.findViewById(R.id.rvcategory)
        val type: TextView = itemView.findViewById(R.id.rvtype)
        val serialNumber: TextView = itemView.findViewById(R.id.rvserialNumber)
        val macAddress: TextView = itemView.findViewById(R.id.rvmacAddress)
        val siteType: TextView = itemView.findViewById(R.id.rvsitetype)
        val country: TextView = itemView.findViewById(R.id.rvcountry)
        val street: TextView = itemView.findViewById(R.id.rvstreet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedDeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_device_detail_list_item_connected_device_item, parent, false)
        return ConnectedDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectedDeviceViewHolder, position: Int) {
        val device = connectedDevices[position]

        holder.category.text = "Category: ${device.category}"
        holder.type.text = "Type: ${device.type}"
        holder.serialNumber.text = "Serial: ${device.serialNumber}"
        holder.macAddress.text = "MAC: ${device.macAddress}"
        holder.siteType.text = "Site Type: ${device.site.type}"
        holder.country.text = "Country: ${device.site.country}"
        holder.street.text = "Street: ${device.site.address.street}"
    }

    override fun getItemCount(): Int = connectedDevices.size

    fun setConnectedDevices(devices: List<DeviceDetailsConnectedDeviceResponse>) {
        this.connectedDevices = devices
        notifyDataSetChanged()
    }

}