package com.gaboardi.githubtest.di

import android.content.Context
import com.gaboardi.githubtest.util.NetworkChecker
import org.koin.dsl.module

fun contextModule(appContext: Context) = module {
    single { NetworkChecker(appContext) }
}