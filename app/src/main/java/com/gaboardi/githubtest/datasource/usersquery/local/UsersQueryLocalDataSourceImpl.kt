package com.gaboardi.githubtest.datasource.usersquery.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.dao.usersquery.UsersQueryDao
import com.gaboardi.githubtest.model.users.User

class UsersQueryLocalDataSourceImpl(
    val usersQueryDao: UsersQueryDao
): UsersQueryLocalDataSource {
    override fun queryUsers(q: String): DataSource.Factory<Int, User> = usersQueryDao.query(if (q.isNotBlank()) "%$q%" else q)
    override fun insert(user: User) = usersQueryDao.insert(user)
    override fun insert(users: List<User>) = usersQueryDao.insert(users)
    override fun count(): Int = usersQueryDao.count()
    override fun clear() = usersQueryDao.clear()
}