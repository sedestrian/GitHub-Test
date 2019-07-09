package com.gaboardi.githubtest.repository.usersquery

import androidx.lifecycle.LiveData
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryLocalDataSource
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse
import com.gaboardi.githubtest.model.base.NetworkBoundResource
import com.gaboardi.githubtest.model.base.Resource
import com.gaboardi.githubtest.util.AppExecutors

class UsersQueryRepositoryImpl(
    val usersQueryRemoteDataSource: UsersQueryRemoteDataSource,
    val usersQueryLocalDataSource: UsersQueryLocalDataSource,
    val appExecutors: AppExecutors
): UsersQueryRepository {
    override fun queryForUsers(q: String): LiveData<Resource<List<User>>> {
        return object: NetworkBoundResource<List<User>, UserQueryResponse>(appExecutors) {
            override fun saveCallResult(item: UserQueryResponse) {
                usersQueryLocalDataSource.insert(item.items)
            }

            override fun shouldFetch(data: List<User>?): Boolean = data == null || data.isEmpty()

            override fun loadFromDb(): LiveData<List<User>> = usersQueryLocalDataSource.queryUsers(q)

            override fun createCall(): LiveData<ApiResponse<UserQueryResponse>> = usersQueryRemoteDataSource.queryUsers(q)
        }.asLiveData()
    }
}