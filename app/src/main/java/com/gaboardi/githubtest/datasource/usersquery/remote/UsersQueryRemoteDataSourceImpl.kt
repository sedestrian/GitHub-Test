package com.gaboardi.githubtest.datasource.usersquery.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gaboardi.githubtest.api.usersquery.UsersQuery
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.DataWrapper
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryBody
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse
import com.gaboardi.githubtest.model.base.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryRemoteDataSourceImpl(
    val api: UsersQuery
): UsersQueryRemoteDataSource {
    override fun queryUsers(q: String): LiveData<ApiResponse<UserQueryResponse>> {
        val data = MutableLiveData<ApiResponse<UserQueryResponse>>()
        api.call(UserQueryBody(q)).enqueue(object: Callback<UserQueryResponse>{
            override fun onFailure(call: Call<UserQueryResponse>, t: Throwable) {
                data.postValue(ApiResponse.create(t))
            }

            override fun onResponse(call: Call<UserQueryResponse>, response: Response<UserQueryResponse>) {
                data.postValue(ApiResponse.create(response))
            }
        })
        return data
    }
}