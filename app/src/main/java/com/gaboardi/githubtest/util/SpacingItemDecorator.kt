package com.gaboardi.githubtest.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(val horizontal: Int, val vertical: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        if(layoutManager is GridLayoutManager) {
            val span = layoutManager.spanCount
            if(parent.getChildAdapterPosition(view) !in 0 until span){
                outRect.top = vertical / 2
            }else{
                outRect.top = vertical
            }
            parent.adapter?.apply {
                if(parent.getChildAdapterPosition(view) !in ((itemCount) - span) until itemCount){
                    outRect.bottom = vertical / 2
                }else{
                    outRect.bottom = vertical
                }
            }
            if(parent.getChildAdapterPosition(view) % span == 0){
                outRect.left = horizontal
            } else {
                outRect.left = horizontal / 2
            }
            if(parent.getChildAdapterPosition(view) % span == span - 1){
                outRect.right = horizontal
            }else{
                outRect.right = horizontal / 2
            }
        }
    }
}