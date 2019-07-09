package com.gaboardi.githubtest.usecases.usersquery

import androidx.lifecycle.LiveData
import com.gaboardi.githubtest.model.DataWrapper
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.Resource
import com.gaboardi.githubtest.repository.usersquery.UsersQueryRepository

class QueryUsersUseCaseImpl(
    val usersQueryRepository: UsersQueryRepository
): QueryUsersUseCase {
    override fun query(q: String): LiveData<Resource<List<User>>> {
        return usersQueryRepository.queryForUsers(q)
    }
}