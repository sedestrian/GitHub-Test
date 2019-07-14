package com.gaboardi.githubtest.datasource.usersquery.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gaboardi.githubtest.api.usersquery.UsersQuery
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryRemoteDataSourceImpl(
    val api: UsersQuery
) : UsersQueryRemoteDataSource {
    override fun queryUsers(): UsersQuery {
        return api
    }
}