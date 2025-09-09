package com.example.thechristianapp.data.repository

import com.example.thechristianapp.data.model.Achievement
import com.example.thechristianapp.data.model.PrayerSession
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    suspend fun logPrayerSession(prayerSession: PrayerSession)
    suspend fun getTotalPrayerCount(): Int
    suspend fun getTotalPoints(): Int?
    suspend fun getCurrentStreak(): Int
    suspend fun getRecentPrayerSessions(limit: Int): List<PrayerSession>
    suspend fun getAllAchievements(): List<Achievement>
    suspend fun unlockAchievement(achievementId: String)
}
