package com.example.thechristianapp.data.repository

import com.example.thechristianapp.data.database.BibleDatabase
import com.example.thechristianapp.data.model.Achievement
import com.example.thechristianapp.data.model.PrayerSession
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerRepositoryImpl @Inject constructor(
    private val database: BibleDatabase
) : PrayerRepository {
    
    override suspend fun logPrayerSession(prayerSession: PrayerSession) {
        database.prayerSessionDao().insertPrayerSession(prayerSession)
        
        // Check for achievements
        checkAndUnlockAchievements()
    }
    
    override suspend fun getTotalPrayerCount(): Int {
        return database.prayerSessionDao().getTotalPrayerCount()
    }
    
    override suspend fun getTotalPoints(): Int? {
        return database.prayerSessionDao().getTotalPoints()
    }
    
    override suspend fun getCurrentStreak(): Int {
        return database.prayerSessionDao().getMaxStreakDay() ?: 0
    }
    
    override suspend fun getRecentPrayerSessions(limit: Int): List<PrayerSession> {
        return database.prayerSessionDao().getAllPrayerSessions().first().take(limit)
    }
    
    override suspend fun getAllAchievements(): List<Achievement> {
        return database.achievementDao().getAllAchievements().first()
    }
    
    override suspend fun unlockAchievement(achievementId: String) {
        database.achievementDao().unlockAchievement(achievementId, System.currentTimeMillis())
    }
    
    private suspend fun checkAndUnlockAchievements() {
        val totalPrayers = getTotalPrayerCount()
        val currentStreak = getCurrentStreak()
        
        // Check for prayer count achievements
        when {
            totalPrayers >= 1 -> unlockAchievement("first_prayer")
            totalPrayers >= 10 -> unlockAchievement("prayer_10")
            totalPrayers >= 50 -> unlockAchievement("prayer_50")
            totalPrayers >= 100 -> unlockAchievement("prayer_100")
        }
        
        // Check for streak achievements
        when {
            currentStreak >= 7 -> unlockAchievement("streak_7")
            currentStreak >= 30 -> unlockAchievement("streak_30")
            currentStreak >= 100 -> unlockAchievement("streak_100")
        }
    }
}
