package com.gaboardi.githubtest.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaboardi.githubtest.adapters.users.ShimmerAdapter

class ShimmerRecycler : RecyclerView {
    private var actualAdapter: Adapter<*>? = null
    private var shimmerAdapter = ShimmerAdapter()
    private var layoutReference: Int = -1
    private var canScroll: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        if (layoutReference != -1)
            shimmerAdapter.setLayout(layoutReference)
        layoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return canScroll
            }
        }
        setShimmerAdapter(true)
    }

    private fun setShimmerAdapter(active: Boolean) {
        if (active) {
            if (adapter !is ShimmerAdapter)
                actualAdapter = adapter
            adapter = shimmerAdapter
        } else {
            adapter = actualAdapter
        }
    }

    fun setLayoutReference(@LayoutRes layout: Int) {
        layoutReference = layout
        shimmerAdapter.setLayout(layoutReference)
    }

    fun shimmer() {
        canScroll = false
        setShimmerAdapter(true)
    }

    fun stopShimmering() {
        canScroll = true
        setShimmerAdapter(false)
    }
}