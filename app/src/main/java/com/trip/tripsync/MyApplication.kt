package com.trip.tripsync

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    init {
        com.trip.tripsync.MyApplication.Companion.instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}