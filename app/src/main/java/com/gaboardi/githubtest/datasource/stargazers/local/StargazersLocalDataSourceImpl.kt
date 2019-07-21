package com.gaboardi.githubtest.datasource.stargazers.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.dao.stargazers.StargazersDao
import com.gaboardi.githubtest.model.stargazers.Stargazer

class StargazersLocalDataSourceImpl(
    val stargazersDao: StargazersDao
): StargazersLocalDataSource {
    override fun queryStargazers(fullRepoName: String): DataSource.Factory<Int, Stargazer> = stargazersDao.queryStargazers()
    override fun insert(stargazers: List<Stargazer>) = stargazersDao.insert(stargazers)
    override fun insert(stargazer: Stargazer) = stargazersDao.insert(stargazer)
    override fun getNextIndex(): Int = stargazersDao.getNextIndex()
    override fun count(): Int = stargazersDao.count()
    override fun clear() = stargazersDao.clear()
}