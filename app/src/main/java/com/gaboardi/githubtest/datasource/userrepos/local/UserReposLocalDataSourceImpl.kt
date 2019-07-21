package com.gaboardi.githubtest.datasource.userrepos.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.dao.userrepos.ReposDao
import com.gaboardi.githubtest.model.userrepos.Repo

class UserReposLocalDataSourceImpl(
    val reposDao: ReposDao
): UserReposLocalDataSource {
    override fun queryRepo(user: String): DataSource.Factory<Int, Repo> = reposDao.queryRepo()

    override fun insert(repos: List<Repo>) = reposDao.insert(repos)

    override fun insert(repo: Repo) = reposDao.insert(repo)

    override fun count(): Int = reposDao.count()

    override fun clear() = reposDao.clear()
}