package com.gaboardi.githubtest.viewmodel.usersquery

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.*
import com.gaboardi.githubtest.usecases.usersquery.QueryUsersUseCase
import java.util.*

class UsersQueryViewModel(
    private val usersUseCase: QueryUsersUseCase
) : ViewModel() {
    private val PAGE_SIZE = 30

    private val query = MutableLiveData<String>()
    private val repoResult = map(query) { usersUseCase.query(it, PAGE_SIZE) }

    val networkState = switchMap(repoResult) { it.networkState }
    val users = switchMap(repoResult) { it.pagedList }
    val refreshState = switchMap(repoResult) { it.refreshState }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            return
        }
        query.value = input
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun currentQuery(): String? = query.value
}