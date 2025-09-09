package com.example.thechristianapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Verse(
    @PrimaryKey val id: Int,
    val chapterId: Int,
    val verseNumber: Int,
    val text: String,
    val isDelivered: Boolean = false,
    val deliveryDate: Long? = null
)
