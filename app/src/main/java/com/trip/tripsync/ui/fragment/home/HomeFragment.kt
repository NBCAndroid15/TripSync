package com.trip.tripsync.ui.fragment.home

import android.content.Context
import android.content.SharedPreferences
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentHomeBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.fragment.CoinFragment
import com.trip.tripsync.ui.fragment.DetailFragment
import com.trip.tripsync.ui.fragment.LoginFragment
import com.trip.tripsync.ui.fragment.SignupFragment
import com.trip.tripsync.ui.fragment.setup.SetupFragment
import com.trip.tripsync.ui.fragment.setup.SharedViewModel
import com.trip.tripsync.viewmodel.FestivalViewModel
import com.trip.tripsync.viewmodel.FestivalViewModelFactory
import com.trip.tripsync.viewmodel.TravelViewModel
import com.trip.tripsync.viewmodel.TravelViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.trip.tripsync.ui.dialog.UserManageFragment
import com.trip.tripsync.ui.fragment.HomeGuideFragment

class HomeFragment : Fragment(),
    HomeFestivalAdapter.onFestivalClick, HomeTravelAdapter.onTravelClick {

    private lateinit var homeAreaAdapter: HomeAreaAdapter
    private var homeTravelAdapter = HomeTravelAdapter(listOf())
    private var homeFestivalAdapter = HomeFestivalAdapter(listOf())
    private val travelViewModel: TravelViewModel by activityViewModels { TravelViewModelFactory() }
    private val festivalViewModel: FestivalViewModel by activityViewModels { FestivalViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var isUserLogIn: Boolean = false


    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 지역
        val areaList = mutableListOf(
            "전체",
            "서울",
            "인천",
            "대전",
            "대구",
            "광주",
            "부산",
            "울산",
            "세종",
            "경기",
            "강원",
            "충북",
            "충남",
            "경북",
            "경남",
            "전북",
            "전남",
            "제주"
        )
        homeAreaAdapter = HomeAreaAdapter(areaList)
        binding.homeCategoryRv.adapter = homeAreaAdapter
        homeAreaAdapter.itemStates[0] = true
        binding.homeCategoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        homeAreaAdapter.itemClick = object : HomeAreaAdapter.ItemClick {
            override fun onClick(keyword: String) {
                travelViewModel.updateKeyword(keyword)
            }
        }

        // 여행
        binding.homeTravelRv.adapter = homeTravelAdapter
        binding.homeTravelRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // 축제
        binding.homeFestivalRv.adapter = homeFestivalAdapter
        binding.homeFestivalRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // 코인 배너
        binding.homeBannerCoin.setOnClickListener {
            val coinFragmentPosition = 3
            val viewPager: ViewPager2 = requireActivity().findViewById(R.id.main_view_pager)
            viewPager.currentItem = coinFragmentPosition
        }

        binding.homeStartplanBtn.setOnClickListener {
            if (isUserLogIn()) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, SetupFragment())
                    .addToBackStack(null)
                    .commit()

                // 홈에서 setup으로 이동할 때 setup 버튼들의 가시성 초기화
                sharedViewModel.setTitleVisible(false)
                sharedViewModel.setUserVisible(false)
                sharedViewModel.setUserCheck(false)

            } else {
                Toast.makeText(requireContext(), "로그인이 필요한 서비스에요!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, LoginFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

//        binding.homeLoginTv.setOnClickListener() {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frame, LoginFragment())
//                .addToBackStack(null)
//                .commit()
//        }


        return binding.root
    }

    private fun isFirstRun(fragmentTag: String): Boolean {
        return sharedPreferences.getBoolean("isFirstRun_$fragmentTag", true)
    }
    private fun setFirstRunFlag(fragmentTag: String) {
        sharedPreferences.edit().putBoolean("isFirstRun_$fragmentTag", false).apply()
    }
    private fun showGuideFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, HomeGuideFragment())
            .commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(
            "com.trip.tripsync.PREFERENCE_FILE_KEY",
            Context.MODE_PRIVATE
        )
        if (isFirstRun("home")) {
            showGuideFragment()
            setFirstRunFlag("home")
        }

        homeTravelAdapter.setOntravelClickListener(this)
        homeFestivalAdapter.setOnFestivalClickListener(this)



        // 여행 아이템 목록 업데이트
        travelViewModel.getTravelList()
        travelViewModel.travelData.observe(viewLifecycleOwner) { travelList ->
            homeTravelAdapter.updateItems(travelList)
        }

        // 축제 아이템 목록 업데이트
        festivalViewModel.getFestivalList()
        festivalViewModel.festivalData.observe(viewLifecycleOwner) { travelList ->
            homeFestivalAdapter.updateItems(travelList)
        }

        val isUserLoggedIn = isUserLogIn()

//        if (isUserLoggedIn) {
//            binding.homeLoginTv.visibility = View.GONE
//        } else {
//            binding.homeLoginTv.visibility = View.VISIBLE
//        }

        homeTravelAdapter.setOntravelClickListener(this)
        homeFestivalAdapter.setOnFestivalClickListener(this)


    }



    override fun onTravelClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onFestivalClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isUserLogIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        return currentUser != null
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