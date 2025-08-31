package com.example.thechristianapp.data.repository

import com.example.thechristianapp.data.model.Book
import com.example.thechristianapp.data.model.Chapter
import com.example.thechristianapp.data.model.Verse
import kotlinx.coroutines.flow.Flow

interface BibleRepository {
    suspend fun cloneBibleContent(): Result<Unit>
    suspend fun forceReloadBibleContent(): Result<Unit>
    fun getAllBooks(): Flow<List<Book>>
    fun getBooksByTestament(testament: String): Flow<List<Book>>
    fun getChaptersByBook(bookId: Int): Flow<List<Chapter>>
    fun getVersesByChapter(chapterId: Int): Flow<List<Verse>>
    suspend fun getRandomUndeliveredVerse(): Verse?
    suspend fun getUndeliveredVerseCount(): Int
    suspend fun searchVerses(query: String): List<Verse>
    suspend fun markVerseAsDelivered(verseId: Int)
    suspend fun getBookById(bookId: Int): Book?
    suspend fun getChapterById(chapterId: Int): Chapter?
    suspend fun getVerseById(verseId: Int): Verse?
    suspend fun isFirstTimeSetup(): Boolean
    suspend fun markFirstTimeSetupComplete()
}
