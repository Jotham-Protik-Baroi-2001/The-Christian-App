package com.example.thechristianapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.repository.BibleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val bibleRepository: BibleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()
    
    init {
        checkFirstTimeSetup()
    }
    
    private fun checkFirstTimeSetup() {
        viewModelScope.launch {
            try {
                _uiState.value = SplashUiState.Loading
                
                val isFirstTime = bibleRepository.isFirstTimeSetup()
                
                if (isFirstTime) {
                    // Try to load Bible content, but don't block app startup if it fails
                    try {
                        val result = bibleRepository.cloneBibleContent()
                        if (result.isSuccess) {
                            bibleRepository.markFirstTimeSetupComplete()
                        }
                        // Continue to success regardless of Bible loading result
                        _uiState.value = SplashUiState.Success
                    } catch (e: Exception) {
                        // Bible loading failed, but allow app to continue
                        _uiState.value = SplashUiState.Success
                    }
                } else {
                    _uiState.value = SplashUiState.Success
                }
            } catch (e: Exception) {
                // Even if everything fails, allow the app to continue
                _uiState.value = SplashUiState.Success
            }
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object Success : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}
