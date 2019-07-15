package com.gaboardi.githubtest.datasource.userrepos.remote

import com.gaboardi.githubtest.api.userrepos.UserRepos

class UserReposRemoteDataSourceImpl(
    val api: UserRepos
): UserReposRemoteDataSource {
    override fun queryRepos(): UserRepos {
        return api
    }
}