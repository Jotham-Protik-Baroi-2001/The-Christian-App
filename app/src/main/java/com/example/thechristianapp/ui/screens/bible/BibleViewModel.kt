package com.example.thechristianapp.ui.screens.bible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.model.Book
import com.example.thechristianapp.data.repository.BibleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BibleViewModel @Inject constructor(
    private val bibleRepository: BibleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<BibleUiState>(BibleUiState.Loading)
    val uiState: StateFlow<BibleUiState> = _uiState.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            try {
                _uiState.value = BibleUiState.Loading
                bibleRepository.getAllBooks().collect { books ->
                    _uiState.value = BibleUiState.Success(books)
                }
            } catch (e: Exception) {
                _uiState.value = BibleUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun selectBook(book: Book, onNavigate: (String) -> Unit) {
        // Navigate to book chapters
        onNavigate("bible/book/${book.id}")
    }
    
    fun forceReloadBible() {
        viewModelScope.launch {
            try {
                _uiState.value = BibleUiState.Loading
                val result = bibleRepository.forceReloadBibleContent()
                if (result.isSuccess) {
                    loadBooks()
                } else {
                    _uiState.value = BibleUiState.Error("Failed to reload Bible: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _uiState.value = BibleUiState.Error("Error: ${e.message}")
            }
        }
    }
}

sealed class BibleUiState {
    object Loading : BibleUiState()
    data class Success(val books: List<Book>) : BibleUiState()
    data class Error(val message: String) : BibleUiState()
}
