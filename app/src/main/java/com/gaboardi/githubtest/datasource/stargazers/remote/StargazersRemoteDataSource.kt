package com.gaboardi.githubtest.datasource.stargazers.remote

import com.gaboardi.githubtest.api.stargazers.Stargazers

interface StargazersRemoteDataSource {
    fun getStargazers(): Stargazers
}