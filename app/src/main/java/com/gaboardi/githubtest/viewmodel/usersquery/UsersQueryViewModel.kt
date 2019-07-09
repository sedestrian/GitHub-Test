package com.gaboardi.githubtest.viewmodel.usersquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.UserQueryResponse
import com.gaboardi.githubtest.model.base.ApiResponse
import com.gaboardi.githubtest.model.base.NetworkBoundResource
import com.gaboardi.githubtest.usecases.usersquery.QueryUsersUseCase

class UsersQueryViewModel(
    val usersUseCase: QueryUsersUseCase
) : ViewModel() {
    fun queryUsers(query: String) = usersUseCase.query(query)
}