package com.gaboardi.githubtest.api.userrepos

import androidx.annotation.IntRange
import com.gaboardi.githubtest.model.userrepos.Repo
import com.gaboardi.githubtest.model.userrepos.RepoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRepos {
    @GET("/users/{user}/repos")
    fun call(
        @Path("user") user: String,
        @Query("page") page: Int? = null,
        @IntRange(from = 1, to = 100) @Query("per_page") perPage: Int? = null
    ): Call<RepoResult>
}