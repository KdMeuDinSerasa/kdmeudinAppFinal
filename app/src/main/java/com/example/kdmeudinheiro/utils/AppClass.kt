package com.example.kdmeudinheiro.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.kdmeudinheiro.services.WorkManagerBuilder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppClass: Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        createScheduler(applicationContext)
    }

    private fun createScheduler(context: Context) {
        WorkManagerBuilder(context).buildService()
    }
}