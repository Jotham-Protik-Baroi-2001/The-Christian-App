package com.example.thechristianapp.ui.screens.bible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thechristianapp.data.model.Book
import com.example.thechristianapp.data.model.Chapter
import com.example.thechristianapp.data.model.Verse
import com.example.thechristianapp.data.repository.BibleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerseViewModel @Inject constructor(
    private val bibleRepository: BibleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<VerseUiState>(VerseUiState.Loading)
    val uiState: StateFlow<VerseUiState> = _uiState.asStateFlow()
    
    private var currentBook: Book? = null
    private var currentChapter: Chapter? = null
    
    fun loadVerses(chapterId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = VerseUiState.Loading
                
                // Get chapter details
                val chapter = bibleRepository.getChapterById(chapterId)
                currentChapter = chapter
                
                if (chapter == null) {
                    _uiState.value = VerseUiState.Error("Chapter not found")
                    return@launch
                }
                
                // Get book details
                val book = bibleRepository.getBookById(chapter.bookId)
                currentBook = book
                
                if (book == null) {
                    _uiState.value = VerseUiState.Error("Book not found")
                    return@launch
                }
                
                // Load verses
                bibleRepository.getVersesByChapter(chapterId).collect { verses ->
                    _uiState.value = VerseUiState.Success(book, chapter, verses)
                }
            } catch (e: Exception) {
                _uiState.value = VerseUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun getCurrentBook(): Book? = currentBook
    fun getCurrentChapter(): Chapter? = currentChapter
}

sealed class VerseUiState {
    object Loading : VerseUiState()
    data class Success(val book: Book, val chapter: Chapter, val verses: List<Verse>) : VerseUiState()
    data class Error(val message: String) : VerseUiState()
}
