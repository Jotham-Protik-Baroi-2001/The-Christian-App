package com.example.thechristianapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thechristianapp.ui.screens.splash.SplashScreen
import com.example.thechristianapp.ui.screens.home.HomeScreen
import com.example.thechristianapp.ui.screens.bible.BibleScreen
import com.example.thechristianapp.ui.screens.bible.ChapterScreen
import com.example.thechristianapp.ui.screens.bible.VerseScreen
import com.example.thechristianapp.ui.screens.dailyverse.DailyVerseScreen
import com.example.thechristianapp.ui.screens.prayer.PrayerScreen
import com.example.thechristianapp.ui.screens.settings.SettingsScreen

@Composable
fun HolyVersesApp() {
    val navController = rememberNavController()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("bible") {
                BibleScreen(navController = navController)
            }
            composable("bible/book/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
                ChapterScreen(navController = navController, bookId = bookId)
            }
            composable("bible/book/{bookId}/chapter/{chapterId}") { backStackEntry ->
                val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 0
                VerseScreen(navController = navController, chapterId = chapterId)
            }
            composable("daily_verse") {
                DailyVerseScreen(navController = navController)
            }
            composable("prayer") {
                PrayerScreen(navController = navController)
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}
