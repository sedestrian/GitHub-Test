package com.gaboardi.githubtest.datasource.usersquery.remote

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.paging.PageKeyedDataSource
import com.gaboardi.githubtest.api.usersquery.UsersQuery
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse

interface UsersQueryRemoteDataSource {
    fun queryUsers(): UsersQuery
}