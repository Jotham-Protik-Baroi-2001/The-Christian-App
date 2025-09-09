package com.example.thechristianapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HolyVersesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app components
    }
}
