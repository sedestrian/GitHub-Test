package com.gaboardi.githubtest.model

data class UserQueryResponse(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)