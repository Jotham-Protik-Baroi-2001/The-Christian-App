package com.example.thechristianapp.ui.screens.dailyverse

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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DailyVerseViewModel @Inject constructor(
    private val bibleRepository: BibleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DailyVerseUiState>(DailyVerseUiState.Loading)
    val uiState: StateFlow<DailyVerseUiState> = _uiState.asStateFlow()
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    init {
        loadDailyVerse()
    }
    
    fun loadDailyVerse() {
        viewModelScope.launch {
            try {
                _uiState.value = DailyVerseUiState.Loading
                
                // Get today's date as seed for consistent daily verse
                val today = dateFormat.format(Date())
                val seed = today.hashCode().toLong()
                
                // Use seed to get a consistent "random" verse for today
                val verse = getDailyVerseForDate(seed)
                
                if (verse != null) {
                    // Get book and chapter info for the verse
                    val chapter = bibleRepository.getChapterById(verse.chapterId)
                    val book = chapter?.let { bibleRepository.getBookById(it.bookId) }
                    
                    if (book != null && chapter != null) {
                        _uiState.value = DailyVerseUiState.Success(
                            verse = verse,
                            book = book,
                            chapter = chapter,
                            date = today
                        )
                    } else {
                        _uiState.value = DailyVerseUiState.Error("Could not load verse details")
                    }
                } else {
                    _uiState.value = DailyVerseUiState.Error("No verses available")
                }
            } catch (e: Exception) {
                _uiState.value = DailyVerseUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    private suspend fun getDailyVerseForDate(seed: Long): Verse? {
        // Use the seed to get a consistent verse for the day
        val random = Random(seed)
        
        // Try to get an undelivered verse first
        val undeliveredVerse = bibleRepository.getRandomUndeliveredVerse()
        if (undeliveredVerse != null) {
            return undeliveredVerse
        }
        
        // Fallback to search-based approach
        return getAnyRandomVerse(random)
    }
    
    private suspend fun getAnyRandomVerse(random: Random): Verse? {
        // Fallback: try to get any verse if no undelivered ones exist
        // This is a simplified implementation using a search query
        try {
            // Search for verses containing common words to get a random selection
            val commonWords = listOf("God", "Lord", "love", "peace", "hope", "faith", "joy", "light")
            val searchWord = commonWords[random.nextInt(commonWords.size)]
            
            val searchResults = bibleRepository.searchVerses(searchWord)
            if (searchResults.isNotEmpty()) {
                return searchResults[random.nextInt(searchResults.size)]
            }
        } catch (e: Exception) {
            // If search fails, return null
        }
        return null
    }
    
    fun shareVerse() {
        val currentState = _uiState.value
        if (currentState is DailyVerseUiState.Success) {
            // TODO: Implement sharing functionality
            // For now, this is a placeholder
        }
    }
    
    fun markAsFavorite() {
        val currentState = _uiState.value
        if (currentState is DailyVerseUiState.Success) {
            // TODO: Implement favorite functionality
            // For now, this is a placeholder
        }
    }
}

sealed class DailyVerseUiState {
    object Loading : DailyVerseUiState()
    data class Success(
        val verse: Verse,
        val book: Book,
        val chapter: Chapter,
        val date: String
    ) : DailyVerseUiState()
    data class Error(val message: String) : DailyVerseUiState()
}
