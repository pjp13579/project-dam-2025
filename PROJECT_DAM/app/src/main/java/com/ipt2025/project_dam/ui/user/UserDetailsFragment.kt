package com.ipt2025.project_dam.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.UsersAPIService
import com.ipt2025.project_dam.databinding.FragmentUserDetailsBinding
import kotlinx.coroutines.launch

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check admin permission
        if (!RetrofitProvider.canViewUsers()) {
            Toast.makeText(context, "Access denied", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        userId = arguments?.getString("_id")

        setupUIBasedOnPermissions()
        loadUserDetails()
        setupClickListeners()
    }

    private fun setupUIBasedOnPermissions() {
        if (!RetrofitProvider.canEditUser()) {
            binding.btnEditUser.visibility = View.GONE
        }

        if (!RetrofitProvider.canDeleteUser()) {
            binding.btnDeleteUser.visibility = View.GONE
        }

        if (!RetrofitProvider.canEditUser() && !RetrofitProvider.canDeleteUser()) {
            binding.userDetailsLinearLayoutHolder.visibility = View.GONE
        }
    }

    private fun loadUserDetails() {
        userId?.let { id ->
            val apiService = RetrofitProvider.create(UsersAPIService::class.java)

            // FIXED: Use viewLifecycleOwner.lifecycleScope instead of lifecycleScope
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.progressBar.visibility = View.VISIBLE
                    val user = apiService.getUser(id)

                    binding.userName.text = "Name: ${user.name}"
                    binding.userEmail.text = "Email: ${user.email}"
                    binding.userRole.text = "Role: ${user.role.capitalize()}"

                    binding.progressBar.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Error loading user: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnEditUser.setOnClickListener {
            if (RetrofitProvider.canEditUser()) {
                userId?.let { id ->
                    val bundle = Bundle().apply {
                        putString("_id", id)
                        putBoolean("isEditMode", true)
                    }
                    findNavController().navigate(
                        R.id.action_userDetailsFragment_to_addEditUserFragment,
                        bundle
                    )
                }
            }
        }

        binding.btnDeleteUser.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete this user? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteUser() {
        // FIXED: Use viewLifecycleOwner.lifecycleScope instead of lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(UsersAPIService::class.java)
                userId?.let { id ->
                    apiService.deleteUser(id)
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "Failed to delete user: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}