package com.gaboardi.githubtest.repository.userrepos

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.gaboardi.githubtest.datasource.userrepos.local.UserReposBoundaryCallback
import com.gaboardi.githubtest.datasource.userrepos.local.UserReposLocalDataSource
import com.gaboardi.githubtest.datasource.userrepos.remote.UserReposRemoteDataSource
import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.userrepos.Repo
import com.gaboardi.githubtest.util.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserReposRepositoryImpl(
    val userReposRemoteDataSource: UserReposRemoteDataSource,
    val userReposLocalDataSource: UserReposLocalDataSource,
    val appExecutors: AppExecutors
): UserReposRepository {
    override fun queryForRepos(user: String, pageSize: Int): Listing<Repo> {
        userReposLocalDataSource.clear()
        val boundaryCallback = UserReposBoundaryCallback(
            appExecutors,
            userReposRemoteDataSource,
            userReposLocalDataSource,
            user,
            { q, list -> saveToDb(list) },
            pageSize
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(user, pageSize)
        }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .setPrefetchDistance(1)
            .build()

        val livePagedList = userReposLocalDataSource.queryRepo(user).toLiveData(
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
                userReposRemoteDataSource.queryRepos().call(query, perPage = pageSize)
                    .enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            networkState.value = NetworkState.error(t.message)
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            appExecutors.diskIO().execute {
                                saveToDb(response.body())
                                networkState.postValue(NetworkState.LOADED)
                            }
                        }
                    })
            }
        }
        return networkState
    }

    private fun saveToDb(repos: List<Repo>?) {
        repos?.let {
            userReposLocalDataSource.insert(repos)
        }
    }
}