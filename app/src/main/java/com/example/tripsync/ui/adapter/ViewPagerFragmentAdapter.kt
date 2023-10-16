package com.example.tripsync.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripsync.ui.fragment.BookmarkManageFragment
import com.example.tripsync.ui.fragment.MyPageFragment
import com.example.tripsync.ui.fragment.SearchFragment
import com.example.tripsync.ui.fragment.home.HomeFragment

class ViewPagerFragmentAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(HomeFragment.newInstance())
        fragmentList.add(BookmarkManageFragment.newInstance())
        fragmentList.add(SearchFragment.newInstance())
        fragmentList.add(MyPageFragment.newInstance())
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}