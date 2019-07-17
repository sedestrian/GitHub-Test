package com.gaboardi.githubtest.api.stargazers

import androidx.annotation.IntRange
import com.gaboardi.githubtest.model.stargazers.Stargazer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Stargazers {
    @GET("/repos/{fullName}/stargazers")
    fun call(
        @Path("fullName", encoded = true) fullName: String,
        @Query("page") page: Int? = null,
        @IntRange(from = 1, to = 100) @Query("per_page") perPage: Int? = null
    ): Call<List<Stargazer>>
}