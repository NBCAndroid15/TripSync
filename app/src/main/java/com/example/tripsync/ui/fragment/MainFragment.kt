package com.example.tripsync.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.tripsync.databinding.FragmentMainBinding
import com.example.tripsync.ui.adapter.ViewPagerFragmentAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val title = arrayOf("홈", "검색", "플랜", "게임", "내 정보")

    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("initPosition", 0) ?: 0

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.mainViewPager.adapter = ViewPagerFragmentAdapter(requireActivity())
        binding.mainViewPager.isUserInputEnabled = false
        binding.mainViewPager.post{binding.mainViewPager.setCurrentItem(position, false)}
        Log.d("position", position.toString())
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            tab.text = title[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}