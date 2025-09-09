package com.example.thechristianapp.data.remote

import com.example.thechristianapp.data.model.Book
import com.example.thechristianapp.data.model.Chapter
import com.example.thechristianapp.data.model.Verse

interface GitService {
    suspend fun loadBibleFromAssets(): Result<Triple<List<Book>, List<Chapter>, List<Verse>>>
}
