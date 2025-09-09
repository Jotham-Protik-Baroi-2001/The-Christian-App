package com.example.thechristianapp.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.thechristianapp.data.repository.BibleRepository
import com.example.thechristianapp.service.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyVerseWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val bibleRepository: BibleRepository,
    private val notificationService: NotificationService
) : CoroutineWorker(appContext, workerParams) {
    
    override suspend fun doWork(): Result {
        return try {
            // Get a random undelivered verse
            val verse = bibleRepository.getRandomUndeliveredVerse()
            
            if (verse != null) {
                // Send notification
                notificationService.sendDailyVerseNotification(verse)
                
                // Mark verse as delivered
                bibleRepository.markVerseAsDelivered(verse.id)
                
                Result.success()
            } else {
                // No more undelivered verses, reset all verses
                // This would need to be implemented in the repository
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
