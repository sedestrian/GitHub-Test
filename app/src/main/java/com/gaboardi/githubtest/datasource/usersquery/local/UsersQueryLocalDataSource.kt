package com.gaboardi.githubtest.datasource.usersquery.local

import androidx.lifecycle.LiveData
import com.gaboardi.githubtest.model.User

interface UsersQueryLocalDataSource {
    fun queryUsers(q: String): LiveData<List<User>>
    fun insert(users: List<User>)
    fun insert(user: User)
}