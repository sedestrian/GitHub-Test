package com.gaboardi.githubtest.datasource.usersquery.remote

import androidx.lifecycle.LiveData
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse

interface UsersQueryRemoteDataSource {
    fun queryUsers(q: String): LiveData<ApiResponse<UserQueryResponse>>
}