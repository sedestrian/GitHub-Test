package com.gaboardi.githubtest.usecases.usersquery

import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.repository.usersquery.UsersQueryRepository

class QueryUsersUseCaseImpl(
    val usersQueryRepository: UsersQueryRepository
) : QueryUsersUseCase {
    override fun query(q: String, pageSize: Int): Listing<User> {
        return usersQueryRepository.queryForUsers(q, pageSize)
    }
}