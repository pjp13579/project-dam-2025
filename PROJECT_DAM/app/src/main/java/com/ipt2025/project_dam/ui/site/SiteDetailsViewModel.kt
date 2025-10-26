package com.ipt2025.project_dam.ui.site

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipt2025.project_dam.data.SitesRepository
import com.ipt2025.project_dam.data.api.SiteDetailResponse
import com.ipt2025.project_dam.data.api.SitesAPIService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed class SiteDetailUiState {
    object Loading : SiteDetailUiState()
    data class Success(val site: SiteDetailResponse) : SiteDetailUiState()
    data class Error(val message: String) : SiteDetailUiState()
}

class SiteDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SiteDetailUiState>(SiteDetailUiState.Loading)
    val uiState: StateFlow<SiteDetailUiState> = _uiState.asStateFlow()

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