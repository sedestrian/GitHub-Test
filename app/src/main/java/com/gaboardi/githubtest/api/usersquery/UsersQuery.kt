package com.gaboardi.githubtest.api.usersquery

import com.gaboardi.githubtest.model.UserQueryBody
import com.gaboardi.githubtest.model.UserQueryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface UsersQuery {
  @GET("/search/users")
  fun call(@Body body: UserQueryBody): Call<UserQueryResponse>
}