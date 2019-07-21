package com.gaboardi.githubtest.usecases.userrepos

import com.gaboardi.githubtest.model.base.Listing
import com.gaboardi.githubtest.model.userrepos.Repo

interface UserReposUseCase {
    fun query(user: String, pageSize: Int): Listing<Repo>
}