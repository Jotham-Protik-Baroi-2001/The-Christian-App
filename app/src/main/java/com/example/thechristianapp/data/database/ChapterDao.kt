package com.example.thechristianapp.data.database

import androidx.room.*
import com.example.thechristianapp.data.model.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE bookId = :bookId ORDER BY chapterNumber")
    fun getChaptersByBook(bookId: Int): Flow<List<Chapter>>
    
    @Query("SELECT * FROM chapters WHERE id = :chapterId")
    suspend fun getChapterById(chapterId: Int): Chapter?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)
    
    @Query("DELETE FROM chapters")
    suspend fun deleteAllChapters()
}
