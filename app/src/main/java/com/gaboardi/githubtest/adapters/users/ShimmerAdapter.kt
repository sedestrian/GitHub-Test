package com.gaboardi.githubtest.adapters.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gaboardi.githubtest.R
import kotlinx.android.synthetic.main.shimmer_layout.view.*

class ShimmerAdapter: RecyclerView.Adapter<ShimmerViewHolder>() {
    private var layoutReference: Int = -1
    private var items: Int = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val shimmerView = inflater.inflate(R.layout.shimmer_layout, parent, false)
        val actualView = inflater.inflate(layoutReference, shimmerView.shimmerContainer, true)
        return ShimmerViewHolder(shimmerView)
    }

    override fun getItemCount(): Int = items

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {}

    fun setLayout(@LayoutRes layout: Int){
        layoutReference = layout
        notifyDataSetChanged()
    }

    fun setItemCount(itemCount: Int){
        items = itemCount
        notifyDataSetChanged()
    }
}