package com.gaboardi.githubtest.datasource.stargazers.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.model.stargazers.Stargazer

interface StargazersLocalDataSource {
    fun queryStargazers(fullRepoName: String): DataSource.Factory<Int, Stargazer>
    fun insert(stargazers: List<Stargazer>)
    fun insert(stargazer: Stargazer)
    fun getNextIndex() : Int
    fun count(): Int
    fun clear()
}