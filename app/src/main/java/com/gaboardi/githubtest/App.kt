package com.gaboardi.githubtest

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.request.target.ViewTarget
import com.gaboardi.githubtest.BuildConfig.BASE_URL
import com.gaboardi.githubtest.db.AppDatabase
import com.gaboardi.githubtest.di.dataModule
import com.gaboardi.githubtest.di.netModule
import com.google.android.material.tabs.TabLayout
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
        ViewTarget.setTagId(R.id.glide_custom_view_target_tag)
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