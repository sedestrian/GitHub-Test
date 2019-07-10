package com.gaboardi.githubtest.repository.usersquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryBoundaryCallback
import com.gaboardi.githubtest.datasource.usersquery.local.UsersQueryLocalDataSource
import com.gaboardi.githubtest.datasource.usersquery.remote.UsersQueryRemoteDataSource
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.util.AppExecutors

class UsersQueryRepositoryImpl(
    val usersQueryRemoteDataSource: UsersQueryRemoteDataSource,
    val usersQueryLocalDataSource: UsersQueryLocalDataSource,
    val appExecutors: AppExecutors
) : UsersQueryRepository {
    override fun queryForUsers(q: String): Listing<User> {
        val boundaryCallback = UsersQueryBoundaryCallback(
            appExecutors,
            usersQueryRemoteDataSource,
            usersQueryLocalDataSource,
            q,
            { q, list -> saveToDb(list) },

        )
        return Listing(MutableLiveData(), MutableLiveData(), MutableLiveData(), {}, {})
    }

    private fun saveToDb(users: List<User>){
        usersQueryLocalDataSource.insert(users)
    }
}