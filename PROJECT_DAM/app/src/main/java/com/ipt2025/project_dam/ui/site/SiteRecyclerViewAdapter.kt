package com.ipt2025.project_dam.ui.site

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.data.api.SiteDataResponse
import com.ipt2025.project_dam.databinding.FragmentSiteListItemBinding

/**
 * ViewAdapter for site list recyclerView list (SiteFragment)
 */
class SiteRecyclerViewAdapter(
    private val sites: MutableList<SiteDataResponse>,
    private val onClick: (SiteDataResponse) -> Unit
) : RecyclerView.Adapter<SiteRecyclerViewAdapter.ViewHolder>() {

    /**
     * viewHolder takes a binding instead of a raw View
     * elements are inserted via the binding and not the view itself
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSiteListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * called constantly as the user scrolls
     * it takes an existing (recycled) ViewHolder and injects it with new data.
     * @param holder The recycled view holder waiting for data
     * @param position The index of the item in the site list we want to show
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sites[position]
        holder.idView.text = item.type
        holder.contentView.text = item.localName
        holder.bind(item)
    }

    override fun getItemCount(): Int = sites.size

    /**
     * adapter "entry point". call this function with data source for adapter to do its thing
     */
    fun addSites(newSites: List<SiteDataResponse>) {
        val start = sites.size
        sites.addAll(newSites)
        notifyItemRangeInserted(start, newSites.size)
    }

    /**
     * defines the binding for the items layout
     */
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
