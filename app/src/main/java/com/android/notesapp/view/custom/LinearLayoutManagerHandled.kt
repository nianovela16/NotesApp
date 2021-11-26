package com.android.notesapp.view.custom

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

class LinearLayoutManagerHandled(ctx: Context,
    tipe: Int, reverse: Boolean): LinearLayoutManager(ctx, tipe, reverse) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "indes out of bound recyclerview")
        }
    }
}