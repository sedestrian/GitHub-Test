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

    /*val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState*/

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            return
        }
//        nextPageHandler.reset()
        query.value = input
    }

    /*fun loadNextPage() {
        _query.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.queryNextPage(it)
            }
        }
    }*/

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun currentQuery(): String? = query.value

    /*class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }
*/
    /*class NextPageHandler(private val repository: RepoRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
            nextPageLiveData = repository.searchNextPage(query)
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = null
                            )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = result.message
                            )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMessage = null
            )
        }
    }*/
}