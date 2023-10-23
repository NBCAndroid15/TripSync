package com.example.tripsync.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripsync.ui.fragment.BookmarkManageFragment
import com.example.tripsync.ui.fragment.FriendManageFragment

class MyPageViewPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

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