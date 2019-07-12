package com.gaboardi.githubtest.adapters.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.R
import com.gaboardi.githubtest.model.base.NetworkState
import com.gaboardi.githubtest.model.base.Status

class NetworkStateItemViewHolder(
    itemView: View,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = itemView.findViewById<Button>(R.id.retry_button)
    private val errorMsg = itemView.findViewById<TextView>(R.id.error_msg)

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        retry.visibility = toVisibility(networkState?.status == Status.FAILED)
        errorMsg.visibility = toVisibility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view, retryCallback)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}