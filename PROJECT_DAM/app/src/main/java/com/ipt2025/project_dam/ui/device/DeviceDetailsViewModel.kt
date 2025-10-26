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

sealed class DeviceDetailsUiState {
    object Loading : DeviceDetailsUiState()
    data class Success(val device: DeviceDetailDataResponse) : DeviceDetailsUiState()
    data class Error(val message: String) : DeviceDetailsUiState()
}

class DeviceDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<DeviceDetailsUiState>(DeviceDetailsUiState.Loading)
    val uiState: StateFlow<DeviceDetailsUiState> = _uiState.asStateFlow()

    fun loadDeviceDetails(api: DevicesAPIService, deviceId: String) {
        viewModelScope.launch {
            _uiState.value = DeviceDetailsUiState.Loading
            try {
                val response = api.getDeviceDetails(deviceId)
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