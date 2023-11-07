package com.trip.tripsync.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trip.tripsync.ui.fragment.BookmarkManageFragment
import com.trip.tripsync.ui.fragment.FriendManageFragment

class MyPageViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(BookmarkManageFragment.newInstance())
        fragmentList.add(FriendManageFragment.newInstance())
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}