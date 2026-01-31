package com.ipt2025.project_dam.ui.site

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.data.api.SiteDataResponse
import com.ipt2025.project_dam.databinding.FragmentSiteListItemBinding

class SiteRecyclerViewAdapter(
    private val sites: MutableList<SiteDataResponse>,
    private val onClick: (SiteDataResponse) -> Unit
) : RecyclerView.Adapter<SiteRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSiteListItemBinding.inflate(
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

    fun addSites(newSites: List<SiteDataResponse>) {
        val start = sites.size
        sites.addAll(newSites)
        notifyItemRangeInserted(start, newSites.size)
    }

    inner class ViewHolder(
        private val binding: FragmentSiteListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        fun bind(site: SiteDataResponse) {
            binding.root.setOnClickListener { onClick(site) }
        }
    }
}
