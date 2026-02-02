package com.ipt2025.project_dam.ui.site

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipt2025.project_dam.data.api.SiteDetailResponse
import com.ipt2025.project_dam.data.api.SitesAPIService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * defines the possible states for the UI after executing API getSiteDetails request
 * ensures the UI it's always in one of those distinct states
 * unlike an enum, sealed classes allow subclasses to hold different types of data
 */
sealed class SiteDetailUiState {
    object Loading : SiteDetailUiState()
    data class Success(val site: SiteDetailResponse) : SiteDetailUiState()
    data class Error(val message: String) : SiteDetailUiState()
}

class SiteDetailsViewModel : ViewModel() {

    // _uiState is 'MutableStateFlow', meaning we can change its value (read/write) inside this class.
    // initialize it with 'Loading' so the screen starts with a spinner.
    private val _uiState = MutableStateFlow<SiteDetailUiState>(SiteDetailUiState.Loading)

    // uiState is exposed as a read-only 'StateFlow'
    // prevents external classes (like the Fragment) from modifying the state.
    // emits updates to observers whenever the value changes
    val uiState: StateFlow<SiteDetailUiState> = _uiState.asStateFlow()

    /**
     * Fetches site details from the API and updates the state accordingly.
     * @param api The Retrofit service interface.
     * @param deviceId The ID of the device to fetch.
     */
    fun loadSiteDetails(api: SitesAPIService, siteId: String) {
        viewModelScope.launch {
            _uiState.value = SiteDetailUiState.Loading
            try {
                val response = api.getSiteDetails(siteId)
                _uiState.value = SiteDetailUiState.Success(response)
            } catch (e: IOException) {
                _uiState.value = SiteDetailUiState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                _uiState.value = SiteDetailUiState.Error("Server error: ${e.message()}")
            } catch (e: Exception) {
                _uiState.value = SiteDetailUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
}