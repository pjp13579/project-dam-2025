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
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentDashboardBinding
import com.ipt2025.project_dam.databinding.FragmentDeviceListBinding
import com.ipt2025.project_dam.databinding.FragmentUserListBinding

/**
 *  simple hub screen with buttons to go to sites, devices, or users paginated list
 */
class Dashboard : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * setup button for navigation if user has permission for such
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sites
        if (RetrofitProvider.canViewSites()) {
            binding.siteListButton.setOnClickListener {
                findNavController().navigate(R.id.action_dashboard_to_siteFragment)
            }
        } else {
            binding.siteListButton.visibility = View.GONE
        }

        // devices
        if (RetrofitProvider.canViewDevices()) {
            binding.deviceListButton.setOnClickListener {
                findNavController().navigate(R.id.action_dashboard_to_deviceFragment)
            }
        } else {
            binding.deviceListButton.visibility = View.GONE
        }

        // users
        if (RetrofitProvider.canViewUsers()) {
            binding.usersListButton.setOnClickListener {
                findNavController().navigate(R.id.action_dashboard_to_userFragment2)
            }
        } else {
            binding.usersListButton.visibility = View.GONE
        }
    }
}