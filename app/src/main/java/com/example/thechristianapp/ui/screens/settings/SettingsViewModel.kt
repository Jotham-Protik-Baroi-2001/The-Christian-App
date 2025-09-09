package com.example.thechristianapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.model.User
import com.example.thechristianapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                // Load user data
                userRepository.getCurrentUser().collect { user ->
                    _uiState.value = _uiState.value.copy(currentUser = user)
                }
                
                // Load other settings from DataStore or SharedPreferences
                // This would be implemented with actual preference storage
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun updateDailyVerseTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(dailyVerseTime = time)
        // Save to preferences
    }
    
    fun updatePrayerReminderTime(time: LocalTime) {
        val currentTimes = _uiState.value.prayerReminderTimes.toMutableList()
        if (!currentTimes.contains(time)) {
            currentTimes.add(time)
            _uiState.value = _uiState.value.copy(prayerReminderTimes = currentTimes)
            // Save to preferences
        }
    }
    
    fun updateDarkMode(isDark: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkMode = isDark)
        // Save to preferences
    }
    
    fun updateFontSize(size: Float) {
        _uiState.value = _uiState.value.copy(fontSize = size)
        // Save to preferences
    }
    
    fun signIn() {
        viewModelScope.launch {
            try {
                // Implement Google Sign-In
                // This would integrate with Firebase Auth
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                userRepository.signOut()
                _uiState.value = _uiState.value.copy(currentUser = null)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class SettingsUiState(
    val dailyVerseTime: LocalTime = LocalTime.of(8, 0),
    val prayerReminderTimes: List<LocalTime> = listOf(
        LocalTime.of(7, 0),
        LocalTime.of(12, 0),
        LocalTime.of(18, 0)
    ),
    val currentUser: User? = null,
    val isDarkMode: Boolean = true,
    val fontSize: Float = 1.0f,
    val appVersion: String = "1.0.0"
)
