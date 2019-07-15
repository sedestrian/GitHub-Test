package com.gaboardi.githubtest.datasource.userrepos.local

import androidx.paging.DataSource
import com.gaboardi.githubtest.model.userrepos.Repo

interface UserReposLocalDataSource {
    fun queryRepo(user: String): DataSource.Factory<Int, Repo>
    fun insert(repos: List<Repo>)
    fun insert(repo: Repo)
    fun count(): Int
    fun clear()
}