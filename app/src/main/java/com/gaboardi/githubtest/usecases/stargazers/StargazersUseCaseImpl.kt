package com.gaboardi.githubtest.usecases.stargazers

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.stargazers.Stargazer
import com.gaboardi.githubtest.repository.stargazers.StargazersRepository
import java.net.URLEncoder

class StargazersUseCaseImpl(
    val stargazersRepository: StargazersRepository
): StargazersUseCase {
    override fun query(repoFullName: String, pageSize: Int): Listing<Stargazer> {
        return stargazersRepository.queryForStargazers(repoFullName, pageSize)
    }
}