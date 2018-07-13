package com.example.namigtahmazli.shrineappnative

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ProductsItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.let {
            it.left = 28 * parent!!.density.i
            it.right = 28 * parent.density.i
            it.top = 10 * parent.density.i
        }
    }
}