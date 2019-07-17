package com.gaboardi.githubtest.viewmodel.stargazers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.gaboardi.githubtest.model.base.Status
import com.gaboardi.githubtest.usecases.stargazers.StargazersUseCase
import com.gaboardi.githubtest.util.NetworkChecker
import java.util.*

class StargazersViewModel(
    private val stargazersUseCase: StargazersUseCase,
    private val networkChecker: NetworkChecker
): ViewModel() {
    private val PAGE_SIZE = 30

    private val repoFullName = MutableLiveData<String>()
    private val repoResult = map(repoFullName) { stargazersUseCase.query(it, PAGE_SIZE) }

    val networkState = switchMap(repoResult) { it.networkState }
    val stargazers = switchMap(repoResult) { it.pagedList }
    val refreshState = switchMap(repoResult) { it.refreshState }

    val networkAvailable = MutableLiveData<Boolean>()

    fun setRepoFullName(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == repoFullName.value) {
            return
        }
        repoFullName.value = input
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun currentQuery(): String? = repoFullName.value

    fun handleNetworkState(state: Status) {
        if (state == Status.FAILED || state == Status.SUCCESS) {
            networkAvailable.postValue(networkChecker.checkForNetwork())
        }
    }
}