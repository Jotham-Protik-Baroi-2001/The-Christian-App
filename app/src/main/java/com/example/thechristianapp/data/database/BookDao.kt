package com.example.thechristianapp.data.database

import androidx.room.*
import com.example.thechristianapp.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY id")
    fun getAllBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE testament = :testament ORDER BY id")
    fun getBooksByTestament(testament: String): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Int): Book?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)
    
    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
    
    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBookCount(): Int
}
