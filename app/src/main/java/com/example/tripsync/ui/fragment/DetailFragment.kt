package com.example.tripsync.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tripsync.R
import com.example.tripsync.data.BookmarkRepositoryImpl
import com.example.tripsync.data.RetrofitInstance.api
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.databinding.FragmentDetailBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.activity.MainActivity
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.example.tripsync.ui.fragment.search.SearchAdapter
import com.example.tripsync.ui.fragment.search.SearchFragment
import com.example.tripsync.ui.fragment.setup.SetupFragment
import com.example.tripsync.viewmodel.MapViewModel
import com.naver.maps.map.MapFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment(val travel: Travel) : Fragment() {

    lateinit var travelXY : Travel

    val bookmarkRepository = BookmarkRepositoryImpl()
    val travelRepository = TravelRepositoryImpl()
    private var _binding: FragmentDetailBinding? = null
    private val viewModel: MapViewModel by viewModels ()
    private val binding: FragmentDetailBinding
        get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun bind(travel: Travel) {
        Glide.with(binding.root.context)
            .load(travel.imageUrl)
            .into(binding.detailIvImage)
        binding.detailTvName.text = travel.title
        binding.detailTvAddr.text = travel.addr
        binding.detailTvRegion.text = travel.area

        viewModel.mapXY.value = travel.mapX!! to travel.mapY!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        bind(travel)

        val contentId = travel.contentId!!
        val contentTypeId = travel.contentTypeId!!

        viewLifecycleOwner.lifecycleScope.launch {
            val detailContentInfo = travelRepository.getTravelDetailInfo(contentId, contentTypeId)
            binding.detailTvContent.text = detailContentInfo
        }


        binding.detailBtnWishList.setOnClickListener {
            val bookmarkManageFragment = BookmarkManageFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment.newInstance())
                .commit()
            fragmentManager.beginTransaction()
                .add(R.id.main_frame, bookmarkManageFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.detailBtnBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()

        }

        binding.detailBtnGoPlan.setOnClickListener {
            val setupFragment = SetupFragment()
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()
            fragmentManager.beginTransaction()
                .add(R.id.main_frame, setupFragment)
                .addToBackStack(null)
                .commit()

        }



        binding.detailBtnBookmark.setOnClickListener() {
            val travelToBookmark = (travel)
            viewLifecycleOwner.lifecycleScope.launch {
                val bookmarkList = bookmarkRepository.addBookmark(travelToBookmark)

                if (bookmarkList != null) {
                    Toast.makeText(requireContext(), "북마크가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "북마크를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(travel: Travel): DetailFragment {
            return DetailFragment(travel)
        }
    }
}
