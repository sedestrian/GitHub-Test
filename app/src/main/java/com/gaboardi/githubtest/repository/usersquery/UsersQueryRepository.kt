package com.gaboardi.githubtest.repository.usersquery

import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.Listing

interface UsersQueryRepository {
    fun queryForUsers(q: String): Listing<User>
}