package com.trip.tripsync.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trip.tripsync.ui.fragment.CoinFragment
import com.trip.tripsync.ui.fragment.MyPageFragment
import com.trip.tripsync.ui.fragment.MyPlanFragment
import com.trip.tripsync.ui.fragment.SearchFragment
import com.trip.tripsync.ui.fragment.home.HomeFragment

class ViewPagerFragmentAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.add(SearchFragment.newInstance())
        fragmentList.add(MyPlanFragment.newInstance())
        fragmentList.add(HomeFragment.newInstance())
        fragmentList.add(CoinFragment.newInstance())
        fragmentList.add(MyPageFragment.newInstance())
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}