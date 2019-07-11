package com.gaboardi.githubtest.adapters.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.databinding.UserItemBinding
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.view.common.DataBoundListAdapter

class UsersAdapter(
    val appExecutors: AppExecutors,
    val onCLick: ((user: User) -> Unit)?,
    val onRetry: () -> Unit
): PagedListAdapter<User, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(User.diffItemCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
){
    private var networkState: NetworkState? = null
    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.user_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.reddit_post_item -> (holder as RedditPostViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }
    /*override fun createBinding(parent: ViewGroup): UserItemBinding {
        val binding = DataBindingUtil.inflate<UserItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.user_item,
            parent,
            false
        )
        return binding
    }

    override fun bind(binding: UserItemBinding, item: User) {
        binding.user = item
        binding.root.setOnClickListener {
            onCLick?.invoke(item)
        }
    }*/
}
