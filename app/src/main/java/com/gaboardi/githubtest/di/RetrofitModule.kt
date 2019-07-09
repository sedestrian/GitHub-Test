package com.gaboardi.githubtest.di

import com.gaboardi.githubtest.BuildConfig
import com.gaboardi.githubtest.api.usersquery.UsersQuery
import com.gaboardi.githubtest.util.AppExecutors
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun netModule(baseUrl: String, timeout: Long = 30) = module {
    single<Gson> { GsonBuilder().create() }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
    }
    single<UsersQuery> { get<Retrofit.Builder>().baseUrl(baseUrl).build().create(UsersQuery::class.java) }
    single<AppExecutors> { AppExecutors() }
}