package com.gaboardi.githubtest.adapters.repos

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.adapters.common.NetworkStateItemViewHolder
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.userrepos.Repo
import com.gaboardi.githubtest.util.AppExecutors

class ReposAdapter(
    val appExecutors: AppExecutors,
    val onCLick: ((user: Repo) -> Unit)?,
    val onRetry: () -> Unit
): PagedListAdapter<Repo, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(Repo.diffItemCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
){
    private var networkState: NetworkState? = null
    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.repo_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.repo_item -> ReposViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, onRetry)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.repo_item -> (holder as ReposViewHolder).bindTo(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
//            val item = getItem(position)
//            (holder as UsersViewHolder)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}