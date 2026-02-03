package com.ipt2025.project_dam.ui.device

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ipt2025.project_dam.data.api.DeviceDataResponse
import com.ipt2025.project_dam.databinding.FragmentDeviceListItemBinding

/**
 * ViewAdapter for device list recyclerView list (DeviceFragment)
 */
class DeviceRecyclerViewAdapter(
    private val devices: MutableList<DeviceDataResponse>,
    private val onClick: (DeviceDataResponse) -> Unit
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>() {

    /**
     * viewHolder takes a binding instead of a raw View
     * elements are inserted via the binding and not the view itself
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentDeviceListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * called constantly as the user scrolls
     * it takes an existing (recycled) ViewHolder and injects it with new data.
     * @param holder The recycled view holder waiting for data
     * @param position The index of the item in the device list we want to show
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size

    /**
     * adapter "entry point". call this function with data source for adapter to do its thing
     */
    fun addDevices(newDevices: List<DeviceDataResponse>) {
        val start = devices.size
        devices.addAll(newDevices)
        notifyItemRangeInserted(start, newDevices.size)
    }

    /**
     * defines the binding for the items layout
     */
    inner class ViewHolder(
        private val binding: FragmentDeviceListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        //data source type received on fun addDevices() -» DeviceDataResponse
        // use subset of those values to inject via the binding
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
