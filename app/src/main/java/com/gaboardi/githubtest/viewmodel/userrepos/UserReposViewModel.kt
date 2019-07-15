package com.gaboardi.githubtest.viewmodel.userrepos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.gaboardi.githubtest.usecases.userrepos.UserReposUseCase
import java.util.*

class UserReposViewModel(
    private val reposUseCase: UserReposUseCase
): ViewModel() {
    private val PAGE_SIZE = 30

    private val user = MutableLiveData<String>()
    private val repoResult = map(user) { reposUseCase.query(it, PAGE_SIZE) }

    val networkState = switchMap(repoResult) { it.networkState }
    val repos = switchMap(repoResult) { it.pagedList }
    val refreshState = switchMap(repoResult) { it.refreshState }

    fun setUser(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == user.value) {
            return
        }
        user.value = input
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun currentQuery(): String? = user.value
}