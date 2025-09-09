package com.example.thechristianapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.thechristianapp.data.model.*

@Database(
    entities = [
        Book::class,
        Chapter::class,
        Verse::class,
        User::class,
        PrayerSession::class,
        Achievement::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BibleDatabase : RoomDatabase() {
    
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun userDao(): UserDao
    abstract fun prayerSessionDao(): PrayerSessionDao
    abstract fun achievementDao(): AchievementDao
    
    companion object {
        @Volatile
        private var INSTANCE: BibleDatabase? = null
        
        fun getDatabase(context: Context): BibleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibleDatabase::class.java,
                    "bible_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
