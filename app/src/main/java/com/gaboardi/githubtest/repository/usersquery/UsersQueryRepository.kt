package com.gaboardi.githubtest.repository.usersquery

import com.gaboardi.githubtest.model.users.User
import com.gaboardi.githubtest.model.base.Listing

interface UsersQueryRepository {
    fun queryForUsers(q: String, pageSize: Int): Listing<User>
}