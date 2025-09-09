package com.example.thechristianapp.data.database

import androidx.room.*
import com.example.thechristianapp.data.model.PrayerSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PrayerSessionDao {
    @Query("SELECT * FROM prayer_sessions ORDER BY actualTime DESC")
    fun getAllPrayerSessions(): Flow<List<PrayerSession>>
    
    @Query("SELECT * FROM prayer_sessions WHERE date = :date")
    fun getPrayerSessionsByDate(date: String): Flow<List<PrayerSession>>
    
    @Query("SELECT * FROM prayer_sessions WHERE date >= :startDate AND date <= :endDate")
    fun getPrayerSessionsByDateRange(startDate: String, endDate: String): Flow<List<PrayerSession>>
    
    @Insert
    suspend fun insertPrayerSession(prayerSession: PrayerSession)
    
    @Update
    suspend fun updatePrayerSession(prayerSession: PrayerSession)
    
    @Delete
    suspend fun deletePrayerSession(prayerSession: PrayerSession)
    
    @Query("SELECT COUNT(*) FROM prayer_sessions WHERE date = :date")
    suspend fun getPrayerCountForDate(date: String): Int
    
    @Query("SELECT COUNT(*) FROM prayer_sessions")
    suspend fun getTotalPrayerCount(): Int
    
    @Query("SELECT MAX(streakDay) FROM prayer_sessions")
    suspend fun getMaxStreakDay(): Int?
    
    @Query("SELECT SUM(pointsEarned) FROM prayer_sessions")
    suspend fun getTotalPoints(): Int?
}
