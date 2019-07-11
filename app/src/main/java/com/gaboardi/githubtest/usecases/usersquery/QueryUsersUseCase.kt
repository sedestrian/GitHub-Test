package com.gaboardi.githubtest.usecases.usersquery

import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.Listing

interface QueryUsersUseCase {
    fun query(q: String, pageSize: Int): Listing<User>
}