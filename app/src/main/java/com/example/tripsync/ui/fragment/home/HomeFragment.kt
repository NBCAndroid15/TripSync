package com.example.tripsync.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentHomeBinding
import com.example.tripsync.ui.fragment.setup.SetupFragment

class HomeFragment : Fragment() {

    private lateinit var homeAreaAdapter: HomeAreaAdapter
    private lateinit var homeTravelAdapter: HomeTravelAdapter
    private lateinit var homeFestivalAdapter: HomeFestivalAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val areaList = mutableListOf("전체", "서울", "경기", "대전", "대구", "부산", "울산", "광주")
        homeAreaAdapter = HomeAreaAdapter(areaList)
        binding.homeCategoryRv.adapter = homeAreaAdapter
        binding.homeCategoryRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        val travelList = mutableListOf<Pair<Any, String>>()
        travelList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        travelList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        travelList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        travelList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        homeTravelAdapter = HomeTravelAdapter(travelList)
        binding.homeTravelRv.adapter = homeTravelAdapter
        binding.homeTravelRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        val festivalList = mutableListOf<Pair<Any, String>>()
        festivalList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        festivalList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        festivalList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        festivalList.add(Pair(R.drawable.item_sample, "제주도 돌하르방 축제"))
        homeFestivalAdapter = HomeFestivalAdapter(festivalList)
        binding.homeFestivalRv.adapter = homeFestivalAdapter
        binding.homeFestivalRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        binding.homeStartplanBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, SetupFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //doSomething
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}