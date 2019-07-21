package com.gaboardi.githubtest.datasource.userrepos.remote

import com.gaboardi.githubtest.api.userrepos.UserRepos

interface UserReposRemoteDataSource {
    fun queryRepos(): UserRepos
}