package com.gaboardi.githubtest.usecases.usersquery

import com.gaboardi.githubtest.model.users.User
import com.gaboardi.githubtest.model.base.Listing

interface QueryUsersUseCase {
    fun query(q: String, pageSize: Int): Listing<User>
}