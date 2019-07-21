package com.gaboardi.githubtest.datasource.stargazers.remote

import com.gaboardi.githubtest.api.stargazers.Stargazers

class StargazersRemoteDataSourceImpl(
    private val stargazers: Stargazers
): StargazersRemoteDataSource {
    override fun getStargazers(): Stargazers {
        return stargazers
    }
}