package com.ipt2025.project_dam.ui.site

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ipt2025.project_dam.R

class SiteDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = SiteDetailsFragment()
    }

    private val viewModel: SiteDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_site_details, container, false)
        val nameView = view.findViewById<TextView>(R.id.site_detail_name)
        nameView.text = arguments?.getString("_id")
        return view
    }
}