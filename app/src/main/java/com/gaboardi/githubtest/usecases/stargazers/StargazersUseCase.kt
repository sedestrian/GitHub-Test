package com.gaboardi.githubtest.usecases.stargazers

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.stargazers.Stargazer

interface StargazersUseCase {
    fun query(repoFullName: String, pageSize: Int): Listing<Stargazer>
}