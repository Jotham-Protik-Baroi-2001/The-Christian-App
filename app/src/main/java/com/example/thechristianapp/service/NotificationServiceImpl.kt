package com.example.thechristianapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.thechristianapp.MainActivity
import com.example.thechristianapp.R
import com.example.thechristianapp.data.model.Verse
import com.example.thechristianapp.worker.DailyVerseWorker
import com.example.thechristianapp.worker.PrayerReminderWorker
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationServiceImpl @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager
) : NotificationService {
    
    companion object {
        private const val DAILY_VERSE_WORK_TAG = "daily_verse"
        private const val PRAYER_REMINDER_WORK_TAG = "prayer_reminder"
        private const val VERSE_CHANNEL_ID = "daily_verse_channel"
        private const val PRAYER_CHANNEL_ID = "prayer_reminder_channel"
        private const val VERSE_NOTIFICATION_ID = 1001
        private const val PRAYER_NOTIFICATION_ID = 1002
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val verseChannel = NotificationChannel(
                VERSE_CHANNEL_ID,
                "Daily Verses",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily Bible verse notifications"
                enableVibration(true)
            }
            
            val prayerChannel = NotificationChannel(
                PRAYER_CHANNEL_ID,
                "Prayer Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Prayer reminder notifications"
                enableVibration(true)
            }
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(verseChannel)
            notificationManager.createNotificationChannel(prayerChannel)
        }
    }
    
    override suspend fun scheduleDailyVerseNotification(time: LocalTime) {
        val now = LocalTime.now()
        var initialDelay = time.toSecondOfDay() - now.toSecondOfDay()
        
        if (initialDelay <= 0) {
            initialDelay += 24 * 60 * 60 // Add 24 hours
        }
        
        val dailyVerseWork = PeriodicWorkRequestBuilder<DailyVerseWorker>(
            1, TimeUnit.DAYS
        ).setInitialDelay(initialDelay.toLong(), TimeUnit.SECONDS)
            .addTag(DAILY_VERSE_WORK_TAG)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            DAILY_VERSE_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyVerseWork
        )
    }
    
    override suspend fun schedulePrayerReminder(time: LocalTime) {
        val now = LocalTime.now()
        var initialDelay = time.toSecondOfDay() - now.toSecondOfDay()
        
        if (initialDelay <= 0) {
            initialDelay += 24 * 60 * 60 // Add 24 hours
        }
        
        val prayerWork = PeriodicWorkRequestBuilder<PrayerReminderWorker>(
            1, TimeUnit.DAYS
        ).setInitialDelay(initialDelay.toLong(), TimeUnit.SECONDS)
            .addTag("${PRAYER_REMINDER_WORK_TAG}_${time.toString()}")
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "prayer_reminder_${time.toString()}",
            ExistingPeriodicWorkPolicy.REPLACE,
            prayerWork
        )
    }
    
    override suspend fun cancelDailyVerseNotification() {
        workManager.cancelAllWorkByTag(DAILY_VERSE_WORK_TAG)
    }
    
    override suspend fun cancelPrayerReminder(time: LocalTime) {
        workManager.cancelAllWorkByTag("${PRAYER_REMINDER_WORK_TAG}_${time.toString()}")
    }
    
    override suspend fun sendDailyVerseNotification(verse: Verse) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, VERSE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Verse")
            .setContentText("${verse.verseNumber}: ${verse.text.take(100)}...")
            .setStyle(NotificationCompat.BigTextStyle().bigText(verse.text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(VERSE_NOTIFICATION_ID, notification)
    }
    
    override suspend fun sendPrayerReminder() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, PRAYER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Prayer Time")
            .setContentText("Time to pray and strengthen your faith!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(PRAYER_NOTIFICATION_ID, notification)
    }
    
    override suspend fun getScheduledNotificationTimes(): List<LocalTime> {
        // This would need to be implemented based on stored preferences
        return emptyList()
    }
    
    override suspend fun getScheduledPrayerTimes(): List<LocalTime> {
        // This would need to be implemented based on stored preferences
        return emptyList()
    }
}
