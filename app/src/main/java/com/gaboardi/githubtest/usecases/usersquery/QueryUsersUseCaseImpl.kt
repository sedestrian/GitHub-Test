package com.gaboardi.githubtest.usecases.usersquery

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.gaboardi.githubtest.model.DataWrapper
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.Resource
import com.gaboardi.githubtest.repository.usersquery.UsersQueryRepository

class QueryUsersUseCaseImpl(
    val usersQueryRepository: UsersQueryRepository
): QueryUsersUseCase {
    override fun query(q: String): LiveData<Resource<PagedList<User>>> {
        return usersQueryRepository.queryForUsers(q)
    }
}