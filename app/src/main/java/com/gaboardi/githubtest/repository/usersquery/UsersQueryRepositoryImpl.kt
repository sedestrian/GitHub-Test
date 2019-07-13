package com.gaboardi.githubtest.repository.usersquery

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryBoundaryCallback
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryLocalDataSource
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.*
import com.gaboardi.githubtest.util.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryRepositoryImpl(
    val usersQueryRemoteDataSource: UsersQueryRemoteDataSource,
    val usersQueryLocalDataSource: UsersQueryLocalDataSource,
    val appExecutors: AppExecutors
) : UsersQueryRepository {
    override fun queryForUsers(q: String, pageSize: Int): Listing<User> {
        MainScope().launch { withContext(Dispatchers.IO){ usersQueryLocalDataSource.clear() } }
        val boundaryCallback = UsersQueryBoundaryCallback(
            appExecutors,
            usersQueryRemoteDataSource,
            usersQueryLocalDataSource,
            q,
            { q, list -> saveToDb(list) },
            pageSize
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(q, pageSize)
        }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = usersQueryLocalDataSource.queryUsers(q).toLiveData(
            config = config,
            boundaryCallback = boundaryCallback)

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    @MainThread
    private fun refresh(query: String, pageSize: Int): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        usersQueryRemoteDataSource.queryUsers().call(query, perPage = pageSize).enqueue(object: Callback<UserQueryResponse>{
            override fun onFailure(call: Call<UserQueryResponse>, t: Throwable) {
                networkState.value = NetworkState.error(t.message)
            }

            override fun onResponse(call: Call<UserQueryResponse>, response: Response<UserQueryResponse>) {
                appExecutors.diskIO().execute {
                    saveToDb(response.body()?.items)
                    networkState.postValue(NetworkState.LOADED)
                }
            }
        })
        return networkState
    }

    private fun saveToDb(users: List<User>?){
        users?.let {
            usersQueryLocalDataSource.insert(users)
        }
    }
}