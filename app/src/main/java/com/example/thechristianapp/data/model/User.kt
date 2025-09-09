package com.example.thechristianapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String, // Google User ID
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val isSignedIn: Boolean,
    val lastSyncTime: Long?,
    val totalPrayerPoints: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)
