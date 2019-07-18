package com.gaboardi.githubtest.datasource.stargazers.local

import androidx.paging.PagedList
import com.gaboardi.githubtest.datasource.stargazers.remote.StargazersRemoteDataSource
import com.gaboardi.githubtest.model.stargazers.Stargazer
import com.gaboardi.githubtest.model.stargazers.StargazerResult
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.util.PagingRequestHelper
import com.gaboardi.githubtest.util.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.ceil

class StargazersBoundaryCallback(
    val appExecutors: AppExecutors,
    val service: StargazersRemoteDataSource,
    val cache: StargazersLocalDataSource,
    val repoFullName: String,
    val handleResponse: (String, List<Stargazer>?) -> Unit,
    val networkPageSize: Int
) : PagedList.BoundaryCallback<Stargazer>() {
    val helper = PagingRequestHelper(appExecutors.diskIO())
    val networkState = helper.createStatusLiveData()
    var loadingEnd = false

    fun resetErrors(){
        helper.clearRequestQueue()
    }

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            service.getStargazers().call(repoFullName, perPage = networkPageSize).enqueue(createWebserviceCallback(callback))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Stargazer) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            val page = ceil(cache.count().toDouble() / networkPageSize) + 1
            println("Loading page $page")
            service.getStargazers().call(repoFullName, page.toInt(), networkPageSize)
                .enqueue(createWebserviceCallback(callback))
        }
    }

    private fun insertItemsIntoDb(
        items: List<Stargazer>,
        it: PagingRequestHelper.Request.Callback
    ) {
        appExecutors.diskIO().execute {
            handleResponse(repoFullName, items)
            it.recordSuccess()
            loadingEnd = false
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<StargazerResult> {
        return object : Callback<StargazerResult> {
            override fun onFailure(
                call: Call<StargazerResult>,
                t: Throwable
            ) {
                println("Failure")
                it.recordFailure(t)
                loadingEnd = false
            }

            override fun onResponse(
                call: Call<StargazerResult>,
                response: Response<StargazerResult>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null && body.users.isNotEmpty() && body.message == null){
                        insertItemsIntoDb(body.users, it)
                    }else it.recordFailure(Throwable("Body empty or error"))
                }else it.recordFailure(Throwable("Call not successful"))
            }
        }
    }
}