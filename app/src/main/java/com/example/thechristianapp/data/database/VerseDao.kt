package com.example.thechristianapp.data.database

import androidx.room.*
import com.example.thechristianapp.data.model.Verse
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {
    @Query("SELECT * FROM verses WHERE chapterId = :chapterId ORDER BY verseNumber")
    fun getVersesByChapter(chapterId: Int): Flow<List<Verse>>
    
    @Query("SELECT * FROM verses WHERE id = :verseId")
    suspend fun getVerseById(verseId: Int): Verse?
    
    @Query("SELECT * FROM verses WHERE isDelivered = 0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomUndeliveredVerse(): Verse?
    
    @Query("SELECT COUNT(*) FROM verses WHERE isDelivered = 0")
    suspend fun getUndeliveredVerseCount(): Int
    
    @Query("SELECT * FROM verses WHERE text LIKE '%' || :query || '%' LIMIT 50")
    suspend fun searchVerses(query: String): List<Verse>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<Verse>)
    
    @Update
    suspend fun updateVerse(verse: Verse)
    
    @Query("UPDATE verses SET isDelivered = 1, deliveryDate = :deliveryDate WHERE id = :verseId")
    suspend fun markVerseAsDelivered(verseId: Int, deliveryDate: Long)
    
    @Query("DELETE FROM verses")
    suspend fun deleteAllVerses()
}
