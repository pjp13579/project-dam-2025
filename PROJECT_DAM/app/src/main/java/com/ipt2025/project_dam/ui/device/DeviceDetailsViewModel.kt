package com.ipt2025.project_dam.ui.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipt2025.project_dam.data.api.DeviceDetailDataResponse
import com.ipt2025.project_dam.data.api.DevicesAPIService
import com.ipt2025.project_dam.data.api.SiteDetailResponse
import com.ipt2025.project_dam.data.api.SitesAPIService
import com.ipt2025.project_dam.ui.site.SiteDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * defines the possible states for the UI after executing API getDeviceDetails request
 * ensures the UI it's always in one of those distinct states
 * unlike an enum, sealed classes allow subclasses to hold different types of data
 */
sealed class DeviceDetailsUiState {
    object Loading : DeviceDetailsUiState()
    data class Success(val device: DeviceDetailDataResponse) : DeviceDetailsUiState()
    data class Error(val message: String) : DeviceDetailsUiState()
}

class DeviceDetailsViewModel : ViewModel() {

    // _uiState is 'MutableStateFlow', meaning we can change its value (read/write) inside this class.
    // initialize it with 'Loading' so the screen starts with a spinner.
    private val _uiState = MutableStateFlow<DeviceDetailsUiState>(DeviceDetailsUiState.Loading)

    // uiState is exposed as a read-only 'StateFlow'
    // prevents external classes (like the Fragment) from modifying the state.
    // emits updates to observers whenever the value changes
    val uiState: StateFlow<DeviceDetailsUiState> = _uiState.asStateFlow()

    /**
     * Fetches device details from the API and updates the state accordingly.
     * @param api The Retrofit service interface.
     * @param deviceId The ID of the device to fetch.
     */
    fun loadDeviceDetails(api: DevicesAPIService, deviceId: String) {
        viewModelScope.launch {
            _uiState.value = DeviceDetailsUiState.Loading
            try {
                val response = api.getDeviceDetails(deviceId) // api call

                // handle result and set sealed UiState accordingly
                _uiState.value = DeviceDetailsUiState.Success(response)
            } catch (e: IOException) {
                _uiState.value = DeviceDetailsUiState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                _uiState.value = DeviceDetailsUiState.Error("Server error: ${e.message()}")
            } catch (e: Exception) {
                _uiState.value = DeviceDetailsUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
}