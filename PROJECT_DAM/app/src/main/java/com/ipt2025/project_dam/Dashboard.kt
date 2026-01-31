package com.ipt2025.project_dam

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController

/**
 *  simple hub screen with buttons to go to sites or devices paginated list
 */

class Dashboard : Fragment() {
    private lateinit var siteButton : Button
    private lateinit var deviceButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)
//
        siteButton = view.findViewById(R.id.site_list_button)
        deviceButton = view.findViewById(R.id.device_list_button)

        siteButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_siteFragment)
        }
        deviceButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_deviceFragment)
        }

        return view;
    }
}