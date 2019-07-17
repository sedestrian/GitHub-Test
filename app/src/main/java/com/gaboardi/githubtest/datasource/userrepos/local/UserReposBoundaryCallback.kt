package com.gaboardi.githubtest.datasource.userrepos.local

import androidx.paging.PagedList
import com.gaboardi.githubtest.datasource.userrepos.remote.UserReposRemoteDataSource
import com.gaboardi.githubtest.model.userrepos.Repo
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.PagingRequestHelper
import com.gaboardi.githubtest.util.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class UserReposBoundaryCallback(
    val appExecutors: AppExecutors,
    val service: UserReposRemoteDataSource,
    val cache: UserReposLocalDataSource,
    val user: String,
    val handleResponse: (String, List<Repo>?) -> Unit,
    val networkPageSize: Int
) : PagedList.BoundaryCallback<Repo>() {
    val helper = PagingRequestHelper(appExecutors.diskIO())
    val networkState = helper.createStatusLiveData()
    var loadingEnd = false

    fun resetErrors(){
        helper.clearRequestQueue()
    }

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            service.queryRepos().call(user, perPage = networkPageSize).enqueue(createWebserviceCallback(callback))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            val page = ceil(cache.count().toDouble() / networkPageSize) + 1
            println("Loading page $page")
            service.queryRepos().call(user, page.toInt(), networkPageSize)
                .enqueue(createWebserviceCallback(callback))
        }
    }

    private fun insertItemsIntoDb(
        response: Response<List<Repo>>,
        it: PagingRequestHelper.Request.Callback
    ) {
        appExecutors.diskIO().execute {
            handleResponse(user, response.body())
            it.recordSuccess()
            loadingEnd = false
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<List<Repo>> {
        return object : Callback<List<Repo>> {
            override fun onFailure(
                call: Call<List<Repo>>,
                t: Throwable
            ) {
                println("Failure")
                it.recordFailure(t)
                loadingEnd = false
            }

            override fun onResponse(
                call: Call<List<Repo>>,
                response: Response<List<Repo>>
            ) {
                println("Success")
                insertItemsIntoDb(response, it)
            }
        }
    }
}