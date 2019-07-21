package com.gaboardi.githubtest.usecases.userrepos

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.userrepos.Repo
import com.gaboardi.githubtest.repository.userrepos.UserReposRepository

class UserReposUseCaseImpl(
    val userReposRepository: UserReposRepository
): UserReposUseCase {
    override fun query(user: String, pageSize: Int): Listing<Repo> {
        return userReposRepository.queryForRepos(user, pageSize)
    }
}