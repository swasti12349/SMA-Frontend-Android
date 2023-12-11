package com.sro.schoolmanagementapp.ItemDecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    // Adjust spacing and offsets as needed
    // The values in this example are just placeholders

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = spacing
        outRect.right = spacing
        outRect.bottom = spacing

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spacing
        } else {
            outRect.top = 0
        }
    }
}
