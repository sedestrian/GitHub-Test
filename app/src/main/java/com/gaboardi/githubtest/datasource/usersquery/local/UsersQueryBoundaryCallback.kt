package com.gaboardi.githubtest.datasource.usersquery.local

import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiEmptyResponse
import com.gaboardi.githubtest.model.base.ApiErrorResponse
import com.gaboardi.githubtest.model.base.ApiResponse
import com.gaboardi.githubtest.model.base.ApiSuccessResponse
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.PagingRequestHelper
import com.gaboardi.githubtest.util.createStatusLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryBoundaryCallback(
    val appExecutors: AppExecutors,
    val service: UsersQueryRemoteDataSource,
    val cache: UsersQueryLocalDataSource,
    val query: String,
    val handleResponse: (String, List<User>?) -> Unit,
    val networkPageSize: Int
) : PagedList.BoundaryCallback<User>() {
    val helper = PagingRequestHelper(appExecutors.diskIO())
    val networkState = helper.createStatusLiveData()
    var loadingEnd = false

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            service.queryUsers().call(query, perPage = networkPageSize).enqueue(createWebserviceCallback(callback))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: User) {
        if(!loadingEnd) {
            loadingEnd
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
                MainScope().launch {
                    withContext(Dispatchers.IO) {
                        val page = cache.count() / networkPageSize
                        service.queryUsers().call(query, page, networkPageSize)
                            .enqueue(createWebserviceCallback(callback))
                    }
                }
            }
        }
    }

    private fun insertItemsIntoDb(
        response: Response<UserQueryResponse>,
        it: PagingRequestHelper.Request.Callback) {
        appExecutors.diskIO().execute {
            handleResponse(query, response.body()?.items)
            it.recordSuccess()
            loadingEnd = false
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<UserQueryResponse> {
        return object : Callback<UserQueryResponse> {
            override fun onFailure(
                call: Call<UserQueryResponse>,
                t: Throwable) {
                it.recordFailure(t)
                loadingEnd = false
            }

            override fun onResponse(
                call: Call<UserQueryResponse>,
                response: Response<UserQueryResponse>) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}