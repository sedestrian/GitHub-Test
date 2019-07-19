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
import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.users.User
import com.gaboardi.githubtest.model.users.UserQueryResponse
import com.gaboardi.githubtest.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryRepositoryImpl(
    val usersQueryRemoteDataSource: UsersQueryRemoteDataSource,
    val usersQueryLocalDataSource: UsersQueryLocalDataSource,
    val appExecutors: AppExecutors
) : UsersQueryRepository {
    override fun queryForUsers(q: String, pageSize: Int): Listing<User> {
        usersQueryLocalDataSource.clear()
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
            .setPrefetchDistance(1)
            .build()

        val livePagedList = usersQueryLocalDataSource.queryUsers(q).toLiveData(
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
        usersQueryRemoteDataSource.queryUsers().call(query, perPage = pageSize)
            .enqueue(object : Callback<UserQueryResponse> {
                override fun onFailure(call: Call<UserQueryResponse>, t: Throwable) {
                    networkState.postValue(NetworkState.error(t.message))
                }

                override fun onResponse(call: Call<UserQueryResponse>, response: Response<UserQueryResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            appExecutors.diskIO().execute {
                                saveToDb(response.body()?.items)
                                networkState.postValue(NetworkState.LOADED)
                            }
                        } else networkState.postValue(NetworkState.error("Body empty or error"))
                    } else networkState.postValue(NetworkState.error("Call not successful"))
                }
            })
        return networkState
    }

    private fun saveToDb(users: List<User>?) {
        users?.let {
            usersQueryLocalDataSource.insert(users)
        }
    }
}