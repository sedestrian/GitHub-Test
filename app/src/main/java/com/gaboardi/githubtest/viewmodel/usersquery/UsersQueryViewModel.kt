package com.gaboardi.githubtest.viewmodel.usersquery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.gaboardi.githubtest.model.base.Status
import com.gaboardi.githubtest.usecases.usersquery.QueryUsersUseCase
import com.gaboardi.githubtest.util.NetworkChecker
import java.util.*

class UsersQueryViewModel(
    private val usersUseCase: QueryUsersUseCase,
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val PAGE_SIZE = 30

    private val query = MutableLiveData<String>()
    private val repoResult = map(query) { usersUseCase.query(it, PAGE_SIZE) }

    val networkState = switchMap(repoResult) { it.networkState }
    val users = switchMap(repoResult) { it.pagedList }
    val refreshState = switchMap(repoResult) { it.refreshState }

    val networkAvailable = MutableLiveData<Boolean>()

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

    fun handleNetworkState(state: Status) {
        if (state == Status.FAILED || state == Status.SUCCESS) {
            networkAvailable.postValue(networkChecker.checkForNetwork())
        }
    }
}