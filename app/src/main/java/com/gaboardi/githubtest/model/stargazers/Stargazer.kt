package com.gaboardi.githubtest.model.stargazers

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stargazers")
data class Stargazer(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("events_url")
    val eventsUrl: String?,
    @SerializedName("followers_url")
    val followersUrl: String?,
    @SerializedName("following_url")
    val followingUrl: String?,
    @SerializedName("gists_url")
    val gistsUrl: String?,
    @SerializedName("gravatar_id")
    val gravatarId: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @PrimaryKey
    val id: Int,
    val login: String?,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("organizations_url")
    val organizationsUrl: String?,
    @SerializedName("received_events_url")
    val receivedEventsUrl: String?,
    @SerializedName("repos_url")
    val reposUrl: String?,
    @SerializedName("site_admin")
    val siteAdmin: Boolean?,
    @SerializedName("starred_url")
    val starredUrl: String?,
    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String?,
    val type: String?,
    val url: String?,
    var callPosition: Int
){
    companion object{
        val diffItemCallback = object : DiffUtil.ItemCallback<Stargazer>() {
            override fun areItemsTheSame(oldItem: Stargazer, newItem: Stargazer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stargazer, newItem: Stargazer): Boolean {
                return oldItem == newItem
            }
        }
    }
}