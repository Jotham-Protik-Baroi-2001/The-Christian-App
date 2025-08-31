package com.example.thechristianapp.data.database

import androidx.room.TypeConverter
import com.example.thechristianapp.data.model.AchievementCategory

class Converters {
    @TypeConverter
    fun fromAchievementCategory(category: AchievementCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toAchievementCategory(category: String): AchievementCategory {
        return AchievementCategory.valueOf(category)
    }
}
