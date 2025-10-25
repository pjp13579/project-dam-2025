package com.ipt2025.project_dam.ui.device

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ipt2025.project_dam.R

class DeviceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DeviceDetailsFragment()
    }

    private val viewModel: DeviceDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }
}