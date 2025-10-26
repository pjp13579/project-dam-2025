package com.ipt2025.project_dam.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.data.api.DeviceDataResponse
import com.ipt2025.project_dam.databinding.FragmentDeviceListItemBinding



class DeviceRecyclerViewAdapter(
    private val values: List<DeviceDataResponse>,
    private val onClick: (DeviceDataResponse) -> Unit
) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceRecyclerViewAdapter.ViewHolder {

        return ViewHolder(
            FragmentDeviceListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: DeviceRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.typeView.text = item.type
        holder.serialNumberView.text = item.serialNumber
        holder.stateView.text = item.state
        holder.bind(item);
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentDeviceListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val typeView: TextView = binding.deviceType
        val serialNumberView: TextView = binding.deviceSerialNumber
        val stateView: TextView = binding.deviceState


        fun bind(device: DeviceDataResponse){
            binding.root.setOnClickListener{ onClick(device) }
        }

        override fun toString(): String {
            return super.toString() + " '" + typeView.text + "'"
        }
    }

}