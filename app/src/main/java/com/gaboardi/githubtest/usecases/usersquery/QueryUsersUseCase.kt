package com.gaboardi.githubtest.usecases.usersquery

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.Resource

interface QueryUsersUseCase {
    fun query(q: String): LiveData<Resource<PagedList<User>>>
}