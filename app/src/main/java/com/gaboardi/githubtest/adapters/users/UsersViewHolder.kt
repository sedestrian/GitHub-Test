package com.gaboardi.githubtest.adapters.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.model.User

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.imageView)
    val name: TextView = itemView.findViewById(R.id.name)

    fun bindTo(user: User?) {
        user?.let {
            Glide.with(itemView).load(it.avatarUrl).into(image)
            name.text = it.login
        }
    }

    companion object{
        fun create(parent: ViewGroup): UsersViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
            return UsersViewHolder(view)
        }
    }
}