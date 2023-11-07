package com.example.tripsync.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentMainBinding
import com.example.tripsync.ui.adapter.ViewPagerFragmentAdapter
import com.example.tripsync.ui.fragment.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val title = arrayOf("검색", "플랜", "홈", "게임", "내 정보")

    private var position = 2
    private var backToExit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("initPosition", 2) ?: 2
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (backToExit) {
                requireActivity().finish()
            } else {
                backToExit = true
                Toast.makeText(requireContext(), "한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    backToExit = false
                }, 2000)
            }
        }
        initView()
    }

    private fun initView() {
        binding.mainViewPager.isUserInputEnabled = false
        binding.mainViewPager.adapter = ViewPagerFragmentAdapter(requireActivity())
        binding.mainViewPager.post { binding.mainViewPager.setCurrentItem(position, false) }
        Log.d("position", position.toString())

        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            val iconResId = when (position) {
                0 -> R.drawable.ic_tab_search2
                1 -> R.drawable.ic_tab_plan2
                2 -> R.drawable.ic_tab_home
                3 -> R.drawable.ic_tab_game2
                4 -> R.drawable.ic_tab_profile2
                else -> null
            }
            tab.icon  = iconResId?.let { resources.getDrawable(it) }
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