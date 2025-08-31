package com.example.thechristianapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: Int,
    val name: String,
    val testament: String, // "OLD" or "NEW"
    val abbreviation: String,
    val chapterCount: Int
)
