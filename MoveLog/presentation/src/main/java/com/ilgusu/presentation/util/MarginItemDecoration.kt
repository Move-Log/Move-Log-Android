package com.ilgusu.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == RecyclerView.NO_POSITION) return

        if (position == 0) {
            // 첫 번째 아이템 -> 왼쪽 마진 추가
            outRect.left = margin
        }
        if (position == itemCount - 1) {
            // 마지막 아이템 -> 오른쪽 마진 추가
            outRect.right = margin
        }
    }
}
