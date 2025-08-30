package com.ecosorter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * EcoSorter Android Application
 * Main entry point for the Android application
 */
@HiltAndroidApp
class EcoSorterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize application components here
    }
}