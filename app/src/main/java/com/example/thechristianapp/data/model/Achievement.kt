package com.example.thechristianapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AchievementCategory {
    STREAK, CONSISTENCY, MILESTONE
}

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconResource: Int,
    val requiredValue: Int, // e.g., 7 for "7-day streak"
    val category: AchievementCategory,
    val isUnlocked: Boolean,
    val unlockedDate: Long?,
    val pointsReward: Int
)
