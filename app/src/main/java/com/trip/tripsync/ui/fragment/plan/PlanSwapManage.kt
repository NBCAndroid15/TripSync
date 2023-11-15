package com.trip.tripsync.ui.fragment.plan

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.ui.adapter.plan.PlanListAdapter

class PlanSwapManage(adapter: PlanListAdapter, context: Context, dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs)
{
    private val dragAdapter = adapter


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        dragAdapter.swapItems(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)

        for (position in 0 until dragAdapter.itemCount) {
            dragAdapter.notifyItemChanged(position)

        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return 0
    }
}