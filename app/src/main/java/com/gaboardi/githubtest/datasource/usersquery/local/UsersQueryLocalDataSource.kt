package com.gaboardi.githubtest.datasource.usersquery.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.model.users.User

interface UsersQueryLocalDataSource {
    fun queryUsers(q: String): DataSource.Factory<Int, User>
    fun insert(users: List<User>)
    fun insert(user: User)
    fun count(): Int
    fun clear()
}