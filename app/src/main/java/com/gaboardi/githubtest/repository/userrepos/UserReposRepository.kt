package com.gaboardi.githubtest.repository.userrepos

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.userrepos.Repo

interface UserReposRepository {
    fun queryForRepos(user: String, pageSize: Int): Listing<Repo>
}