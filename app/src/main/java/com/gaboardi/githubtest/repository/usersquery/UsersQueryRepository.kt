package com.gaboardi.githubtest.repository.usersquery

import androidx.lifecycle.LiveData
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.Resource

interface UsersQueryRepository {
    fun queryForUsers(q: String): LiveData<Resource<List<User>>>
}