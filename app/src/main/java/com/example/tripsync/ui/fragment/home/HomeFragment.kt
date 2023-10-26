package com.example.tripsync.ui.fragment.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentHomeBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.DetailFragment
import com.example.tripsync.ui.fragment.setup.SetupFragment
import com.example.tripsync.viewmodel.FestivalViewModel
import com.example.tripsync.viewmodel.FestivalViewModelFactory
import com.example.tripsync.viewmodel.TravelViewModel
import com.example.tripsync.viewmodel.TravelViewModelFactory

class HomeFragment : Fragment(),
    HomeFestivalAdapter.onFestivalClick, HomeTravelAdapter.onTravelClick {

    private lateinit var homeAreaAdapter: HomeAreaAdapter
    private var homeTravelAdapter = HomeTravelAdapter(listOf())
    private var homeFestivalAdapter = HomeFestivalAdapter(listOf())
    private val travelViewModel: TravelViewModel by activityViewModels { TravelViewModelFactory() }
    private val festivalViewModel: FestivalViewModel by activityViewModels { FestivalViewModelFactory() }


    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        // 지역
        val areaList = mutableListOf("전체", "서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종", "경기", "강원", "충북", "충남", "경북", "경남", "전북", "전남", "제주")
        homeAreaAdapter = HomeAreaAdapter(areaList)
        binding.homeCategoryRv.adapter = homeAreaAdapter
        binding.homeCategoryRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        homeAreaAdapter.itemClick = object : HomeAreaAdapter.ItemClick {
            override fun onClick(keyword: String) {
                travelViewModel.updateKeyword(keyword)
            }
        }

        // 여행
        binding.homeTravelRv.adapter = homeTravelAdapter
        binding.homeTravelRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        // 축제
        binding.homeFestivalRv.adapter = homeFestivalAdapter
        binding.homeFestivalRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        binding.homeStartplanBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frame, SetupFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeTravelAdapter.setOntravelClickListener (this)
        homeFestivalAdapter.setOnFestivalClickListener(this)

        // 여행 아이템 목록 업데이트
        travelViewModel.getTravelList()
        travelViewModel.travelData.observe(viewLifecycleOwner) { travelList ->

            requireActivity().runOnUiThread {
                homeTravelAdapter.updateItems(travelList)
            }
        }

        // 축제 아이템 목록 업데이트
        festivalViewModel.getFestivalList()
        festivalViewModel.festivalData.observe(viewLifecycleOwner) { travelList ->

            requireActivity().runOnUiThread {
                homeFestivalAdapter.updateItems(travelList)
            }
        }
    }

    override fun onTravelClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onFestivalClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
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