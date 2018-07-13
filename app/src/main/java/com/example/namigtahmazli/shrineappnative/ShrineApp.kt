package com.example.namigtahmazli.shrineappnative

import android.app.Application

class ShrineApp : Application() {
    companion object {
        lateinit var instance: ShrineApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}