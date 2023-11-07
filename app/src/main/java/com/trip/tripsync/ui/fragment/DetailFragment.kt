package com.trip.tripsync.ui.fragment

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
import com.trip.tripsync.R
import com.trip.tripsync.data.BookmarkRepositoryImpl
import com.trip.tripsync.data.RetrofitInstance.api
import com.trip.tripsync.data.TravelRepositoryImpl
import com.trip.tripsync.databinding.FragmentDetailBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.activity.MainActivity
import com.trip.tripsync.ui.fragment.plan.PlanFragment
import com.trip.tripsync.ui.fragment.search.SearchAdapter
import com.trip.tripsync.ui.fragment.search.SearchFragment
import com.trip.tripsync.ui.fragment.setup.SetupFragment
import com.trip.tripsync.viewmodel.MapViewModel
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
            .error(R.drawable.item_error)
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
            val mainFragment = MainFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val bundle = Bundle()
            bundle.putInt("initPosition", 4)
            mainFragment.arguments = bundle

            fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.main_frame, mainFragment)
                .commit()
        }

        binding.detailBtnBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()

        }

        binding.detailBtnGoPlan.setOnClickListener {
            val setupFragment = SetupFragment()
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()
            fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
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
