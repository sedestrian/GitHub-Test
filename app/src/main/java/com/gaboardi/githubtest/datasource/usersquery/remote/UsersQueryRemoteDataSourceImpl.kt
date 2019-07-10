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
    override fun queryUsers(q: String, page: Int?, perPage: Int?): LiveData<ApiResponse<UserQueryResponse>> {
        val data = MutableLiveData<ApiResponse<UserQueryResponse>>()
        api.call(q, page, perPage).enqueue(object : Callback<UserQueryResponse> {
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