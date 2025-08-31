package com.example.thechristianapp.di

import android.content.Context
import androidx.room.Room
import com.example.thechristianapp.data.database.BibleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideBibleDatabase(@ApplicationContext context: Context): BibleDatabase {
        return Room.databaseBuilder(
            context,
            BibleDatabase::class.java,
            "bible_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideBookDao(database: BibleDatabase) = database.bookDao()
    
    @Provides
    fun provideChapterDao(database: BibleDatabase) = database.chapterDao()
    
    @Provides
    fun provideVerseDao(database: BibleDatabase) = database.verseDao()
    
    @Provides
    fun provideUserDao(database: BibleDatabase) = database.userDao()
    
    @Provides
    fun providePrayerSessionDao(database: BibleDatabase) = database.prayerSessionDao()
    
    @Provides
    fun provideAchievementDao(database: BibleDatabase) = database.achievementDao()
}
