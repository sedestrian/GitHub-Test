package com.gaboardi.githubtest.datasource.usersquery.remote

import com.gaboardi.githubtest.api.usersquery.UsersQuery

interface UsersQueryRemoteDataSource {
    fun queryUsers(): UsersQuery
}