package com.ipt2025.project_dam.ui.site

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SiteCreateRequest
import com.ipt2025.project_dam.data.api.SiteUpdateRequest
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.databinding.FragmentSiteAddEditBinding
import kotlinx.coroutines.launch

class AddEditSiteFragment : Fragment() {

    private var _binding: FragmentSiteAddEditBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isEditMode = false
    private var siteId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Get mode from arguments
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            siteId = it.getString("_id")
        }

        // check permission and navigate back if unauthorized
        if (isEditMode && !RetrofitProvider.canEditSite() || !isEditMode && !RetrofitProvider.canCreateSite()) {
            Toast.makeText(context, "Access denied", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupButton()

        // If edit mode, load existing data
        if (isEditMode && siteId != null) {
            loadSiteData(siteId!!)
        }
    }

    private fun setupButton() {
        binding.btnSave.text = if (isEditMode) "Update Site" else "Create Site"

        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                if (isEditMode) {
                    updateSite()
                } else {
                    createSite()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Clear previous errors
        binding.etLocalName.error = null
        binding.etType.error = null
        binding.etCountry.error = null

        // Required fields validation
        if (binding.etLocalName.text.isNullOrEmpty()) {
            binding.etLocalName.error = "Local name is required"
            isValid = false
        }

        if (binding.etType.text.isNullOrEmpty()) {
            binding.etType.error = "Type is required"
            isValid = false
        }

        if (binding.etCountry.text.isNullOrEmpty()) {
            binding.etCountry.error = "Country is required"
            isValid = false
        }

        // Address validation (if any field is filled, all must be filled)
        val hasAnyAddressField = listOf(
            binding.etStreet.text,
            binding.etCity.text,
            binding.etState.text,
            binding.etZipcode.text,
            binding.etLatitude.text,
            binding.etLongitude.text
        ).any { !it.isNullOrEmpty() }

        val hasAllAddressFields = listOf(
            binding.etStreet.text,
            binding.etCity.text,
            binding.etState.text,
            binding.etZipcode.text,
            binding.etLatitude.text,
            binding.etLongitude.text
        ).all { !it.isNullOrEmpty() }

        if (hasAnyAddressField && !hasAllAddressFields) {
            Toast.makeText(requireContext(),
                "Please fill all address fields",
                Toast.LENGTH_LONG
            ).show()
            isValid = false
        }

        return isValid
    }

    private fun createSite() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)

                // Prepare address (optional)
                val address = if (
                    !binding.etStreet.text.isNullOrEmpty() &&
                    !binding.etCity.text.isNullOrEmpty() &&
                    !binding.etState.text.isNullOrEmpty() &&
                    !binding.etZipcode.text.isNullOrEmpty() &&
                    !binding.etLatitude.text.isNullOrEmpty() &&
                    !binding.etLongitude.text.isNullOrEmpty()
                ) {
                    com.ipt2025.project_dam.data.api.SiteAddressRequest(
                        street = binding.etStreet.text.toString(),
                        city = binding.etCity.text.toString(),
                        state = binding.etState.text.toString(),
                        zipCode = binding.etZipcode.text.toString(),
                        latitude = binding.etLatitude.text.toString().toFloat(),
                        longitude = binding.etLongitude.text.toString().toFloat()
                    )
                } else {
                    null
                }

                val siteRequest = SiteCreateRequest(
                    localName = binding.etLocalName.text.toString(),
                    type = binding.etType.text.toString(),
                    country = binding.etCountry.text.toString(),
                    address = address,
                    devicesAtSite = emptyList(), // Empty for new site
                    isActive = true
                )

                val response = apiService.createSite(siteRequest)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Site created successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to create site: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSite() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)
                siteId?.let { id ->
                    // Prepare address (optional)
                    val address = if (
                        !binding.etStreet.text.isNullOrEmpty() &&
                        !binding.etCity.text.isNullOrEmpty() &&
                        !binding.etState.text.isNullOrEmpty() &&
                        !binding.etZipcode.text.isNullOrEmpty() &&
                        !binding.etLatitude.text.isNullOrEmpty() &&
                        !binding.etLongitude.text.isNullOrEmpty()
                    ) {
                        com.ipt2025.project_dam.data.api.SiteAddressRequest(
                            street = binding.etStreet.text.toString(),
                            city = binding.etCity.text.toString(),
                            state = binding.etState.text.toString(),
                            zipCode = binding.etZipcode.text.toString(),
                            latitude = binding.etLatitude.text.toString().toFloat(),
                            longitude = binding.etLongitude.text.toString().toFloat()
                        )
                    } else {
                        null
                    }

                    val siteRequest = SiteUpdateRequest(
                        localName = binding.etLocalName.text.toString(),
                        type = binding.etType.text.toString(),
                        country = binding.etCountry.text.toString(),
                        address = address,
                        isActive = true
                    )

                    val response = apiService.updateSite(id, siteRequest)

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Site updated successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Failed to update site: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadSiteData(siteId: String) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitProvider.create(SitesAPIService::class.java)
                val response = apiService.getSiteDetails(siteId)

                // Populate fields with existing data
                binding.etLocalName.setText(response.localName)
                binding.etType.setText(response.type)
                binding.etCountry.setText(response.country)

                // Populate address if exists
                response.address?.let { address ->
                    binding.etStreet.setText(address.street)
                    binding.etCity.setText(address.city)
                    binding.etState.setText(address.state)
                    binding.etZipcode.setText(address.zipCode)
                    binding.etLatitude.setText(address.latitude.toString())
                    binding.etLongitude.setText(address.longitude.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load site data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}