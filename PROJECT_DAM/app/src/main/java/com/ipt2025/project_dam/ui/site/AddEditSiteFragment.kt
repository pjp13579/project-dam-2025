package com.ipt2025.project_dam.ui.site

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ipt2025.project_dam.R
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.data.api.SiteCreateRequest
import com.ipt2025.project_dam.data.api.SiteUpdateRequest
import com.ipt2025.project_dam.data.api.SitesAPIService
import kotlinx.coroutines.launch

class AddEditSiteFragment : Fragment() {

    private lateinit var etLocalName: TextInputEditText
    private lateinit var etType: TextInputEditText
    private lateinit var etCountry: TextInputEditText
    private lateinit var etStreet: TextInputEditText
    private lateinit var etCity: TextInputEditText
    private lateinit var etState: TextInputEditText
    private lateinit var etZipcode: TextInputEditText
    private lateinit var etLatitude: TextInputEditText
    private lateinit var etLongitude: TextInputEditText
    private lateinit var btnSave: MaterialButton

    private var isEditMode = false
    private var siteId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_site, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get mode from arguments
        arguments?.let {
            isEditMode = it.getBoolean("isEditMode", false)
            siteId = it.getString("_id")
        }

        // Initialize views
        etLocalName = view.findViewById(R.id.et_local_name)
        etType = view.findViewById(R.id.et_type)
        etCountry = view.findViewById(R.id.et_country)
        etStreet = view.findViewById(R.id.et_street)
        etCity = view.findViewById(R.id.et_city)
        etState = view.findViewById(R.id.et_state)
        etZipcode = view.findViewById(R.id.et_zipcode)
        etLatitude = view.findViewById(R.id.et_latitude)
        etLongitude = view.findViewById(R.id.et_longitude)
        btnSave = view.findViewById(R.id.btn_save)

        setupButton()

        // If edit mode, load existing data
        if (isEditMode && siteId != null) {
            loadSiteData(siteId!!)
        }
    }

    private fun setupButton() {
        btnSave.text = if (isEditMode) "Update Site" else "Create Site"

        btnSave.setOnClickListener {
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
        etLocalName.error = null
        etType.error = null
        etCountry.error = null

        // Required fields validation
        if (etLocalName.text.isNullOrEmpty()) {
            etLocalName.error = "Local name is required"
            isValid = false
        }

        if (etType.text.isNullOrEmpty()) {
            etType.error = "Type is required"
            isValid = false
        }

        if (etCountry.text.isNullOrEmpty()) {
            etCountry.error = "Country is required"
            isValid = false
        }

        // Address validation (if any field is filled, all must be filled)
        val hasAnyAddressField = listOf(
            etStreet.text,
            etCity.text,
            etState.text,
            etZipcode.text,
            etLatitude.text,
            etLongitude.text
        ).any { !it.isNullOrEmpty() }

        val hasAllAddressFields = listOf(
            etStreet.text,
            etCity.text,
            etState.text,
            etZipcode.text,
            etLatitude.text,
            etLongitude.text
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
                    !etStreet.text.isNullOrEmpty() &&
                    !etCity.text.isNullOrEmpty() &&
                    !etState.text.isNullOrEmpty() &&
                    !etZipcode.text.isNullOrEmpty() &&
                    !etLatitude.text.isNullOrEmpty() &&
                    !etLongitude.text.isNullOrEmpty()
                ) {
                    com.ipt2025.project_dam.data.api.SiteAddressRequest(
                        street = etStreet.text.toString(),
                        city = etCity.text.toString(),
                        state = etState.text.toString(),
                        zipCode = etZipcode.text.toString(),
                        latitude = etLatitude.text.toString().toFloat(),
                        longitude = etLongitude.text.toString().toFloat()
                    )
                } else {
                    null
                }

                val siteRequest = SiteCreateRequest(
                    localName = etLocalName.text.toString(),
                    type = etType.text.toString(),
                    country = etCountry.text.toString(),
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
                        !etStreet.text.isNullOrEmpty() &&
                        !etCity.text.isNullOrEmpty() &&
                        !etState.text.isNullOrEmpty() &&
                        !etZipcode.text.isNullOrEmpty() &&
                        !etLatitude.text.isNullOrEmpty() &&
                        !etLongitude.text.isNullOrEmpty()
                    ) {
                        com.ipt2025.project_dam.data.api.SiteAddressRequest(
                            street = etStreet.text.toString(),
                            city = etCity.text.toString(),
                            state = etState.text.toString(),
                            zipCode = etZipcode.text.toString(),
                            latitude = etLatitude.text.toString().toFloat(),
                            longitude = etLongitude.text.toString().toFloat()
                        )
                    } else {
                        null
                    }

                    val siteRequest = SiteUpdateRequest(
                        localName = etLocalName.text.toString(),
                        type = etType.text.toString(),
                        country = etCountry.text.toString(),
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
                etLocalName.setText(response.localName)
                etType.setText(response.type)
                etCountry.setText(response.country)

                // Populate address if exists
                response.address?.let { address ->
                    etStreet.setText(address.street)
                    etCity.setText(address.city)
                    etState.setText(address.state)
                    etZipcode.setText(address.zipCode)
                    etLatitude.setText(address.latitude.toString())
                    etLongitude.setText(address.longitude.toString())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load site data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}