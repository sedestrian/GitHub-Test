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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersQueryBoundaryCallback(
    val appExecutors: AppExecutors,
    val service: UsersQueryRemoteDataSource,
    val cache: UsersQueryLocalDataSource,
    val query: String,
    val handleResponse: (String, List<User>) -> Unit,
    val networkPageSize: Int
) : PagedList.BoundaryCallback<User>() {
    val helper = PagingRequestHelper(appExecutors.diskIO())
    val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            Transformations.map(service.queryUsers(query, perPage = networkPageSize)){
                handleApi(callback, it)
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: User) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){ callback ->
            val page = cache.count() / networkPageSize
            Transformations.map(service.queryUsers(query, page, networkPageSize)){
                handleApi(callback, it)
            }
        }
    }

    private fun insertItemsIntoDb(
        response: ApiSuccessResponse<UserQueryResponse>,
        it: PagingRequestHelper.Request.Callback) {
        appExecutors.diskIO().execute {
            handleResponse(query, response.body.items)
            it.recordSuccess()
        }
    }

    private fun handleApi(
        it: PagingRequestHelper.Request.Callback,
        response: ApiResponse<UserQueryResponse>
    ){
        when(response){
            is ApiSuccessResponse -> {
                insertItemsIntoDb(response, it)
            }
            is ApiEmptyResponse -> {
                it.recordFailure(Throwable("Empty response"))
            }
            is ApiErrorResponse -> {
                it.recordFailure(Throwable(response.errorMessage))
            }
        }
    }
}