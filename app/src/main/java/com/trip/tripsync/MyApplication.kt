package com.trip.tripsync

import android.app.Application

class MyApplication : Application() {
    init {
        com.trip.tripsync.MyApplication.Companion.instance = this
    }

    companion object {
        lateinit var instance: Application
    }
}