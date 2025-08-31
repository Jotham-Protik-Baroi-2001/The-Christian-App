package com.example.thechristianapp.data.repository

import android.content.Context
import android.util.Log
import com.example.thechristianapp.data.database.BibleDatabase
import com.example.thechristianapp.data.model.*
import com.example.thechristianapp.data.remote.GitService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BibleRepositoryImpl @Inject constructor(
    private val database: BibleDatabase,
    private val gitService: GitService,
    @ApplicationContext private val context: Context
) : BibleRepository {
    
    companion object {
        private const val TAG = "BibleRepository"
        private const val PREFS_NAME = "bible_prefs"
        private const val KEY_FIRST_TIME_SETUP = "first_time_setup"
    }
    
    override suspend fun cloneBibleContent(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting Bible content loading from assets...")
            
            // Load Bible data from assets
            val bibleData = gitService.loadBibleFromAssets()
            if (bibleData.isFailure) {
                Log.e(TAG, "Failed to load Bible data from assets: ${bibleData.exceptionOrNull()}")
                return@withContext Result.failure(bibleData.exceptionOrNull() ?: Exception("Failed to load Bible data"))
            }
            
            val (books, chapters, verses) = bibleData.getOrThrow()
            
            // Clear existing data and insert new data
            database.bookDao().deleteAllBooks()
            database.chapterDao().deleteAllChapters()
            database.verseDao().deleteAllVerses()
            
            database.bookDao().insertBooks(books)
            database.chapterDao().insertChapters(chapters)
            database.verseDao().insertVerses(verses)
            
            // Mark as completed in preferences
            val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPrefs.edit().putBoolean(KEY_FIRST_TIME_SETUP, false).apply()
            
            Log.d(TAG, "Bible content loaded from assets successfully: ${books.size} books, ${chapters.size} chapters, ${verses.size} verses")
            Result.success(Unit)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Bible content from assets", e)
            Result.failure(e)
        }
    }
    
    override suspend fun forceReloadBibleContent(): Result<Unit> {
        Log.d(TAG, "Force reloading Bible content...")
        // Reset first time setup flag to force reload
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_TIME_SETUP, true).apply()
        return cloneBibleContent()
    }
    
    override fun getAllBooks(): Flow<List<Book>> = database.bookDao().getAllBooks()
    
    override fun getBooksByTestament(testament: String): Flow<List<Book>> = 
        database.bookDao().getBooksByTestament(testament)
    
    override fun getChaptersByBook(bookId: Int): Flow<List<Chapter>> = 
        database.chapterDao().getChaptersByBook(bookId)
    
    override fun getVersesByChapter(chapterId: Int): Flow<List<Verse>> = 
        database.verseDao().getVersesByChapter(chapterId)
    
    override suspend fun getRandomUndeliveredVerse(): Verse? = 
        database.verseDao().getRandomUndeliveredVerse()
    
    override suspend fun getUndeliveredVerseCount(): Int = 
        database.verseDao().getUndeliveredVerseCount()
    
    override suspend fun searchVerses(query: String): List<Verse> = 
        database.verseDao().searchVerses(query)
    
    override suspend fun markVerseAsDelivered(verseId: Int) {
        database.verseDao().markVerseAsDelivered(verseId, System.currentTimeMillis())
    }
    
    override suspend fun getBookById(bookId: Int): Book? = 
        database.bookDao().getBookById(bookId)
    
    override suspend fun getChapterById(chapterId: Int): Chapter? = 
        database.chapterDao().getChapterById(chapterId)
    
    override suspend fun getVerseById(verseId: Int): Verse? = 
        database.verseDao().getVerseById(verseId)
    
    override suspend fun isFirstTimeSetup(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean(KEY_FIRST_TIME_SETUP, true) // Default to true for first time
        Log.d(TAG, "isFirstTimeSetup: $isFirstTime")
        return isFirstTime
    }
    
    override suspend fun markFirstTimeSetupComplete() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_TIME_SETUP, false).apply()
        Log.d(TAG, "First time setup marked as complete")
    }
}
