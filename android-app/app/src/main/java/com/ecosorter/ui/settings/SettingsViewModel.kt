package com.ecosorter.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosorter.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        // Load settings from preferences or default values
        _uiState.update {
            it.copy(
                language = "中文",
                theme = "系统默认",
                notificationsEnabled = true,
                analyticsEnabled = true,
                versionName = Constants.VERSION_NAME,
                versionCode = Constants.VERSION_CODE
            )
        }
    }

    fun toggleNotifications() {
        _uiState.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }
        // Save to preferences
    }

    fun toggleAnalytics() {
        _uiState.update { it.copy(analyticsEnabled = !it.analyticsEnabled) }
        // Save to preferences
    }

    fun setLanguage(language: String) {
        _uiState.update { it.copy(language = language) }
        // Save to preferences
    }

    fun setTheme(theme: String) {
        _uiState.update { it.copy(theme = theme) }
        // Save to preferences
    }
}

data class SettingsUiState(
    val language: String = "中文",
    val theme: String = "系统默认",
    val notificationsEnabled: Boolean = true,
    val analyticsEnabled: Boolean = true,
    val versionName: String = "1.0.0",
    val versionCode: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)