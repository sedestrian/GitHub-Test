package com.gaboardi.githubtest

import android.app.Application
import androidx.room.Room
import com.gaboardi.githubtest.BuildConfig.BASE_URL
import com.gaboardi.githubtest.db.AppDatabase
import com.gaboardi.githubtest.di.dataModule
import com.gaboardi.githubtest.di.netModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Timber.plant(Timber.DebugTree())
        initializeKoin()
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    private fun initializeKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(netModule(BASE_URL), dataModule))
        }
    }

    companion object {
        lateinit var INSTANCE: App
    }
}