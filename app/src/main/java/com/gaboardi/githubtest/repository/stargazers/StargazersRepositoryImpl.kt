package com.gaboardi.githubtest.repository.stargazers

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.gaboardi.githubtest.datasource.stargazers.local.StargazersBoundaryCallback
import com.gaboardi.githubtest.datasource.stargazers.local.StargazersLocalDataSource
import com.gaboardi.githubtest.datasource.stargazers.remote.StargazersRemoteDataSource
import com.gaboardi.githubtest.db.AppDatabase
import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.stargazers.Stargazer
import com.gaboardi.githubtest.model.stargazers.StargazerResult
import com.gaboardi.githubtest.util.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StargazersRepositoryImpl(
    val userReposRemoteDataSource: StargazersRemoteDataSource,
    val userReposLocalDataSource: StargazersLocalDataSource,
    val appDatabase: AppDatabase,
    val appExecutors: AppExecutors
): StargazersRepository {
    override fun queryForStargazers(repoFullName: String, pageSize: Int): Listing<Stargazer> {
        userReposLocalDataSource.clear()
        val boundaryCallback = StargazersBoundaryCallback(
            appExecutors,
            userReposRemoteDataSource,
            userReposLocalDataSource,
            repoFullName,
            { q, list -> saveToDb(list) },
            pageSize
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(repoFullName, pageSize)
        }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .setPrefetchDistance(1)
            .build()

        val livePagedList = userReposLocalDataSource.queryStargazers(repoFullName).toLiveData(
            config = config,
            boundaryCallback = boundaryCallback
        )

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                boundaryCallback.resetErrors()
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    @MainThread
    private fun refresh(query: String, pageSize: Int): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        MainScope().launch {
            withContext(Dispatchers.IO){
                userReposLocalDataSource.clear()
                userReposRemoteDataSource.getStargazers().call(query, perPage = pageSize)
                    .enqueue(object : Callback<StargazerResult> {
                        override fun onFailure(call: Call<StargazerResult>, t: Throwable) {
                            networkState.value = NetworkState.error(t.message)
                        }

                        override fun onResponse(call: Call<StargazerResult>, response: Response<StargazerResult>) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null && body.users.isNotEmpty()) {
                                    appExecutors.diskIO().execute {
                                        saveToDb(response.body()?.users)
                                        networkState.postValue(NetworkState.LOADED)
                                    }
                                } else networkState.postValue(NetworkState.error("Body empty or error"))
                            } else networkState.postValue(NetworkState.error("Call not successful"))
                        }
                    })
            }
        }
        return networkState
    }

    private fun saveToDb(repos: List<Stargazer>?) {
        repos?.let {
            appDatabase.runInTransaction {
                val start = userReposLocalDataSource.getNextIndex()
                val data = repos.mapIndexed{ index, stargazer ->
                    stargazer.callPosition = start + index
                    stargazer
                }
                userReposLocalDataSource.insert(data)
            }
        }
    }
}