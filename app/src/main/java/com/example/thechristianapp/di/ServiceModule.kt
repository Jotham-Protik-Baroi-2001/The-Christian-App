package com.example.thechristianapp.di

import androidx.work.WorkManager
import com.example.thechristianapp.service.NotificationService
import com.example.thechristianapp.service.NotificationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    
    @Binds
    abstract fun bindNotificationService(
        notificationServiceImpl: NotificationServiceImpl
    ): NotificationService
}

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {
    
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
