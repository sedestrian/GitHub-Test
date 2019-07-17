package com.gaboardi.githubtest.datasource.usersquery.remote

import com.gaboardi.githubtest.api.usersquery.UsersQuery

class UsersQueryRemoteDataSourceImpl(
    val api: UsersQuery
) : UsersQueryRemoteDataSource {
    override fun queryUsers(): UsersQuery {
        return api
    }
}