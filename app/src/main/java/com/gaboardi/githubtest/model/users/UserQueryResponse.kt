package com.gaboardi.githubtest.model.users

import com.gaboardi.githubtest.model.users.User

data class UserQueryResponse(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int,
    val message: String?
)