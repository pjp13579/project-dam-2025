package com.ipt2025.project_dam.ui.site

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.data.api.SiteDataResponse

import com.ipt2025.project_dam.ui.site.placeholder.PlaceholderContent.PlaceholderItem
import com.ipt2025.project_dam.databinding.FragmentSiteBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SiteRecyclerViewAdapter(
    private val values: List<SiteDataResponse>,
    private val onClick: (SiteDataResponse) -> Unit
) : RecyclerView.Adapter<SiteRecyclerViewAdapter.ViewHolder>() {

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
        val item = values[position]
        holder.idView.text = item.type
        holder.contentView.text = item.localName
        holder.bind(item);
    }

    override fun getItemCount(): Int = values.size

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