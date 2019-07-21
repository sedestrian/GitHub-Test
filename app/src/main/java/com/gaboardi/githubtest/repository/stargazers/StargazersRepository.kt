package com.gaboardi.githubtest.repository.stargazers

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.stargazers.Stargazer

interface StargazersRepository {
    fun queryForStargazers(repoFullName: String, pageSize: Int): Listing<Stargazer>
}