package com.gaboardi.githubtest.adapters.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.model.userrepos.Repo

class ReposViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title)
    val description: TextView = itemView.findViewById(R.id.description)
    val stargazers: TextView = itemView.findViewById(R.id.stargazers)

    fun bindTo(repo: Repo?, onCLick: ((user: Repo) -> Unit)?) {
        repo?.let {
            title.text = it.name
            if(it.description.isNullOrBlank()){
                description.isGone = true
            }else {
                description.text = it.description
                description.isVisible = true
            }
            stargazers.text = it.stargazersCount.toString()
            itemView.setOnClickListener { view -> onCLick?.invoke(it) }
        }
    }

    companion object{
        fun create(parent: ViewGroup): ReposViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_item, parent, false)
            return ReposViewHolder(view)
        }
    }
}