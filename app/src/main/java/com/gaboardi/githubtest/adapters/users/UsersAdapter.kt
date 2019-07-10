package com.gaboardi.githubtest.adapters.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.databinding.UserItemBinding
import com.gaboardi.githubtest.model.User
import com.gaboardi.githubtest.util.AppExecutors
import com.gaboardi.githubtest.view.common.DataBoundListAdapter

class UsersAdapter(
    val appExecutors: AppExecutors,
    val onCLick: ((user: User) -> Unit)?
): DataBoundListAdapter<User, UserItemBinding>(
    appExecutors = appExecutors,
    diffCallback = User.diffItemCallback
){
    override fun createBinding(parent: ViewGroup): UserItemBinding {
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
    }

}
