package com.example.thechristianapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_sessions")
data class PrayerSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scheduledTime: String, // HH:mm format
    val actualTime: Long, // Timestamp when actually prayed
    val loggedTime: Long, // Timestamp when user logged it
    val pointsEarned: Int,
    val streakDay: Int,
    val isManualEntry: Boolean,
    val date: String // YYYY-MM-DD format
)
