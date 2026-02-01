package com.ipt2025.project_dam.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.EditUserRequest
import com.ipt2025.project_dam.data.api.RegisterUserRequest
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.UsersAPIService
import com.ipt2025.project_dam.databinding.FragmentUserAddEditBinding
import kotlinx.coroutines.launch

class AddEditUserFragment : Fragment() {

    private var _binding: FragmentUserAddEditBinding? = null
    private val binding get() = _binding!!

    private var userId: String? = null
    private var isEditMode: Boolean = false

    // Available roles
    private val roles = listOf("guest", "technician", "admin")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check admin permission
        if (!RetrofitProvider.canCreateUser() && !RetrofitProvider.canEditUser()) {
            Toast.makeText(context, "Access denied", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        // Get arguments
        userId = arguments?.getString("_id")
        isEditMode = arguments?.getBoolean("isEditMode", false) ?: false

        setupRoleDropdown()
        setupUI()

        if (isEditMode && userId != null) {
            loadUserData()
        }

        setupClickListeners()
    }

    private fun setupUI() {
        // Update title based on mode
        binding.topAppBar.title = if (isEditMode) {
            getString(R.string.title_edit_user)
        } else {
            getString(R.string.title_add_user)
        }

        // In edit mode, make password optional
        if (isEditMode) {
            binding.passwordInputLayout.helperText = getString(R.string.helper_password_optional)
        }
    }

    private fun setupRoleDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            roles
        )
        binding.roleDropdown.setAdapter(adapter)

        // Set default to "user" for new users
        if (!isEditMode) {
            binding.roleDropdown.setText("guest", false)
        }
    }

    private fun loadUserData() {
        userId?.let { id ->
            val apiService = RetrofitProvider.create(UsersAPIService::class.java)

            viewLifecycleOwner.lifecycleScope.launch {  // CHANGED
                try {
                    binding.progressBar.visibility = View.VISIBLE
                    val user = apiService.getUser(id)

                    binding.etName.setText(user.name)
                    binding.etEmail.setText(user.email ?: "")
                    binding.roleDropdown.setText(user.role, false)

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
        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                if (isEditMode) {
                    updateUser()
                } else {
                    createUser()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val role = binding.roleDropdown.text.toString().trim()

        // Name validation
        if (name.isEmpty()) {
            binding.etName.error = getString(R.string.error_name_required)
            binding.etName.requestFocus()
            return false
        }

        // Email validation
        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.error_email_required)
            binding.etEmail.requestFocus()
            return false
        }

        // Password validation (required for new users, optional for edit)
        if (!isEditMode && password.isEmpty()) {
            binding.etPassword.error = getString(R.string.error_password_required)
            binding.etPassword.requestFocus()
            return false
        }

        if (password.isNotEmpty() && password.length < 6) {
            binding.etPassword.error = getString(R.string.error_password_length)
            binding.etPassword.requestFocus()
            return false
        }

        // Role validation
        if (role.isEmpty() || !roles.contains(role)) {
            Toast.makeText(context, getString(R.string.error_role_required), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val role = binding.roleDropdown.text.toString().trim()

        val request = RegisterUserRequest(
            name = name,
            email = email,
            password = password,
            role = role
        )

        val apiService = RetrofitProvider.create(UsersAPIService::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSave.isEnabled = false

                val response = apiService.registerUser(request)

                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    getString(R.string.success_user_created),
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().popBackStack()

            } catch (e: Exception) {
                e.printStackTrace()
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true

                Toast.makeText(
                    context,
                    getString(R.string.error_creating_user, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateUser() {
        userId?.let { id ->
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val role = binding.roleDropdown.text.toString().trim()

            val request = EditUserRequest(
                name = name,
                email = email,
                password = if (password.isNotEmpty()) password else null,
                role = role
            )

            val apiService = RetrofitProvider.create(UsersAPIService::class.java)

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSave.isEnabled = false

                    val response = apiService.updateUser(id, request)

                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        getString(R.string.success_user_updated),
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().popBackStack()

                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.isEnabled = true

                    Toast.makeText(
                        context,
                        getString(R.string.error_updating_user, e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}