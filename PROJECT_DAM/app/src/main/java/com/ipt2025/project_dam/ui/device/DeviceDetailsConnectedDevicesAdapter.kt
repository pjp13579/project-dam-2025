package com.ipt2025.project_dam.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.data.api.DeviceDetailsConnectedDeviceResponse
import com.ipt2025.project_dam.databinding.FragmentDeviceDetailListItemConnectedDeviceItemBinding

/**
 * ViewAdapter for device details connected devices recyclerView list (DeviceDetailsFragment)
 */
class DeviceDetailsConnectedDevicesAdapter : RecyclerView.Adapter<DeviceDetailsConnectedDevicesAdapter.ConnectedDeviceViewHolder>()  {

    private var connectedDevices: List<DeviceDetailsConnectedDeviceResponse> = emptyList()

    /**
     * viewHolder takes a binding instead of a raw View
     * elements are inserted via the binding and not the view itself
     */
    inner class ConnectedDeviceViewHolder(val binding: FragmentDeviceDetailListItemConnectedDeviceItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    /**
     * called when the RecyclerView needs a new items to display (example, when scrolling up/down)
     * executed only enough times to fill the screen, not for every item in the list
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedDeviceViewHolder {
        // add elements via the binding, not the view
        val binding = FragmentDeviceDetailListItemConnectedDeviceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConnectedDeviceViewHolder(binding)
    }

    /**
     * called constantly as the user scrolls
     * it takes an existing (recycled) ViewHolder and injects it with new data.
     * @param holder The recycled view holder waiting for data
     * @param position The index of the item in the 'connectedDevices' list we want to show
     */
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

    /**
     * adapter "entry point". call this function with data source for adapter to do its thing
     */
    fun setConnectedDevices(devices: List<DeviceDetailsConnectedDeviceResponse>) {
        this.connectedDevices = devices
        notifyDataSetChanged() // trigger UI change
    }
}