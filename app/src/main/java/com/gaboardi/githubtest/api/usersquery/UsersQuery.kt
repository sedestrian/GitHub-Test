package com.gaboardi.githubtest.api.usersquery

import androidx.annotation.IntRange
import com.gaboardi.githubtest.model.UserQueryBody
import com.gaboardi.githubtest.model.UserQueryResponse
import retrofit2.Call
import retrofit2.http.*

interface UsersQuery {
  @GET("/search/users")
  fun call(
    @Query("q") query: String,
    @Query("page") page: Int? = null,
    @IntRange(from = 1, to = 100) @Query("per_page") perPage: Int? = null
  ): Call<UserQueryResponse>
}