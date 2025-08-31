package com.example.thechristianapp.ui.screens.bible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.model.Book
import com.example.thechristianapp.data.model.Chapter
import com.example.thechristianapp.data.repository.BibleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val bibleRepository: BibleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ChapterUiState>(ChapterUiState.Loading)
    val uiState: StateFlow<ChapterUiState> = _uiState.asStateFlow()
    
    private var currentBook: Book? = null
    
    fun loadChapters(bookId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = ChapterUiState.Loading
                
                // Get book details
                val book = bibleRepository.getBookById(bookId)
                currentBook = book
                
                if (book == null) {
                    _uiState.value = ChapterUiState.Error("Book not found")
                    return@launch
                }
                
                // Load chapters
                bibleRepository.getChaptersByBook(bookId).collect { chapters ->
                    _uiState.value = ChapterUiState.Success(book, chapters)
                }
            } catch (e: Exception) {
                _uiState.value = ChapterUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun selectChapter(chapter: Chapter, onNavigate: (String) -> Unit) {
        // Navigate to chapter verses
        onNavigate("bible/book/${chapter.bookId}/chapter/${chapter.id}")
    }
    
    fun getCurrentBook(): Book? = currentBook
}

sealed class ChapterUiState {
    object Loading : ChapterUiState()
    data class Success(val book: Book, val chapters: List<Chapter>) : ChapterUiState()
    data class Error(val message: String) : ChapterUiState()
}
