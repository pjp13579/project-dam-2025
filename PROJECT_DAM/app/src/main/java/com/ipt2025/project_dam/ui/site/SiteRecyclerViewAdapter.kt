package com.ipt2025.project_dam.ui.site

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.data.api.SiteDataResponse
import com.ipt2025.project_dam.databinding.FragmentSiteBinding

class SiteRecyclerViewAdapter(
    private val onClick: (SiteDataResponse) -> Unit
) : RecyclerView.Adapter<SiteRecyclerViewAdapter.ViewHolder>() {

    // Store sites in a mutable list
    private var sites: List<SiteDataResponse> = emptyList()

    // Function to update the list
    fun submitList(newSites: List<SiteDataResponse>) {
        sites = newSites
        notifyDataSetChanged() // tells RecyclerView to refresh
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSiteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sites[position]
        holder.idView.text = item.type
        holder.contentView.text = item.localName
        holder.bind(item)
    }

    override fun getItemCount(): Int = sites.size

    inner class ViewHolder(private val binding: FragmentSiteBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        fun bind(site: SiteDataResponse){
            binding.root.setOnClickListener{ onClick(site) }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}