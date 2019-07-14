package com.gaboardi.githubtest.datasource.usersquery.local

import androidx.paging.PagedList
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.PagingRequestHelper
import com.gaboardi.githubtest.util.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.floor

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
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            val page = ceil(cache.count().toDouble() / networkPageSize) + 1
            println("Loading page $page")
            service.queryUsers().call(query, page.toInt(), networkPageSize)
                .enqueue(createWebserviceCallback(callback))
        }
    }

    private fun insertItemsIntoDb(
        response: Response<UserQueryResponse>,
        it: PagingRequestHelper.Request.Callback
    ) {
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
                t: Throwable
            ) {
                println("Failure")
                it.recordFailure(t)
                loadingEnd = false
            }

            override fun onResponse(
                call: Call<UserQueryResponse>,
                response: Response<UserQueryResponse>
            ) {
                println("Success")
                insertItemsIntoDb(response, it)
            }
        }
    }
}