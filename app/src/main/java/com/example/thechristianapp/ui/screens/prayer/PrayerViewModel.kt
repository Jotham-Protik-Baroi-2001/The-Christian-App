package com.example.thechristianapp.ui.screens.prayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.model.Achievement
import com.example.thechristianapp.data.model.PrayerSession
import com.example.thechristianapp.data.repository.PrayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PrayerUiState())
    val uiState: StateFlow<PrayerUiState> = _uiState.asStateFlow()
    
    init {
        loadPrayerData()
    }
    
    fun loadPrayerData() {
        viewModelScope.launch {
            try {
                // Load prayer statistics
                val totalPrayers = prayerRepository.getTotalPrayerCount()
                val totalPoints = prayerRepository.getTotalPoints() ?: 0
                val currentStreak = prayerRepository.getCurrentStreak()
                
                // Load recent prayers
                val recentPrayers = prayerRepository.getRecentPrayerSessions(10)
                
                // Load achievements
                val achievements = prayerRepository.getAllAchievements()
                
                _uiState.value = _uiState.value.copy(
                    totalPrayers = totalPrayers,
                    totalPoints = totalPoints,
                    currentStreak = currentStreak,
                    recentPrayers = recentPrayers,
                    achievements = achievements
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun logPrayer() {
        viewModelScope.launch {
            try {
                val prayerSession = PrayerSession(
                    scheduledTime = "00:00",
                    actualTime = System.currentTimeMillis(),
                    loggedTime = System.currentTimeMillis(),
                    pointsEarned = 10, // Base points
                    streakDay = _uiState.value.currentStreak + 1,
                    isManualEntry = true,
                    date = LocalDate.now().toString()
                )
                
                prayerRepository.logPrayerSession(prayerSession)
                
                // Reload data
                loadPrayerData()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class PrayerUiState(
    val currentStreak: Int = 0,
    val totalPoints: Int = 0,
    val totalPrayers: Int = 0,
    val recentPrayers: List<PrayerSession> = emptyList(),
    val achievements: List<Achievement> = emptyList()
)
