package com.gaboardi.githubtest.viewmodel.stargazers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.gaboardi.githubtest.usecases.stargazers.StargazersUseCase
import java.util.*

class StargazersViewModel(
    val stargazersUseCase: StargazersUseCase
): ViewModel() {
    private val PAGE_SIZE = 30

    private val repoFullName = MutableLiveData<String>()
    private val repoResult = map(repoFullName) { stargazersUseCase.query(it, PAGE_SIZE) }

    val networkState = switchMap(repoResult) { it.networkState }
    val stargazers = switchMap(repoResult) { it.pagedList }
    val refreshState = switchMap(repoResult) { it.refreshState }

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
}