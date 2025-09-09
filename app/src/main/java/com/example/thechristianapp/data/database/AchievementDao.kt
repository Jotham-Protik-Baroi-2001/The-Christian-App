package com.example.thechristianapp.data.database

import androidx.room.*
import com.example.thechristianapp.data.model.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY requiredValue")
    fun getAllAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievements(): Flow<List<Achievement>>
    
    @Query("SELECT * FROM achievements WHERE id = :achievementId")
    suspend fun getAchievementById(achievementId: String): Achievement?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<Achievement>)
    
    @Update
    suspend fun updateAchievement(achievement: Achievement)
    
    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :unlockedDate WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: String, unlockedDate: Long)
}
