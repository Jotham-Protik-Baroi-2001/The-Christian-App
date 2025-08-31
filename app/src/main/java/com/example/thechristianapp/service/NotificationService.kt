package com.example.thechristianapp.service

import com.example.thechristianapp.data.model.Verse
import java.time.LocalTime

interface NotificationService {
    suspend fun scheduleDailyVerseNotification(time: LocalTime)
    suspend fun schedulePrayerReminder(time: LocalTime)
    suspend fun cancelDailyVerseNotification()
    suspend fun cancelPrayerReminder(time: LocalTime)
    suspend fun sendDailyVerseNotification(verse: Verse)
    suspend fun sendPrayerReminder()
    suspend fun getScheduledNotificationTimes(): List<LocalTime>
    suspend fun getScheduledPrayerTimes(): List<LocalTime>
}
