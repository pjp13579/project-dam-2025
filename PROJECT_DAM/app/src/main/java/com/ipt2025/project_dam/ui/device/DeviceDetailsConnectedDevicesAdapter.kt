package com.ipt2025.project_dam.ui.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
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

    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedDeviceViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_device_detail_list_item_connected_device_item, parent, false)
        return ConnectedDeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectedDeviceViewHolder, position: Int) {
        val device = connectedDevices[position]
        val ctx = context!!;

        holder.category.text = ctx.getString(R.string.label_category).replace("{value}", device.category)
        holder.type.text = ctx.getString(R.string.label_type).replace("{value}", device.type)
        holder.serialNumber.text = ctx.getString(R.string.label_serial_number).replace("{value}", device.serialNumber)
        holder.macAddress.text = ctx.getString(R.string.label_mac_address).replace("{value}", device.macAddress)
        holder.siteType.text = ctx.getString(R.string.label_site_type).replace("{value}", device.site.type)
        holder.country.text = ctx.getString(R.string.label_country).replace("{value}", device.site.country)
        holder.street.text = ctx.getString(R.string.label_street).replace("{value}", device.site.address.street)
    }

    override fun getItemCount(): Int = connectedDevices.size

    fun setConnectedDevices(devices: List<DeviceDetailsConnectedDeviceResponse>) {
        this.connectedDevices = devices
        notifyDataSetChanged()
    }

}