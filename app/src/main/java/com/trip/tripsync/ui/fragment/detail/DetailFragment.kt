package com.trip.tripsync.ui.fragment.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.data.BookmarkRepositoryImpl
import com.trip.tripsync.data.TravelRepositoryImpl
import com.trip.tripsync.databinding.FragmentDetailBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.fragment.MainFragment
import com.trip.tripsync.ui.fragment.setup.SetupFragment
import com.trip.tripsync.viewmodel.MapViewModel
import kotlinx.coroutines.launch

class DetailFragment(val travel: Travel, val menuVisible: Boolean) : Fragment() {

    lateinit var travelXY: Travel

    val bookmarkRepository = BookmarkRepositoryImpl()
    val travelRepository = TravelRepositoryImpl()
    private var _binding: FragmentDetailBinding? = null
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapFragment: MapFragment
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

        if (!menuVisible) {
            binding.detailBtnGoPlan.visibility = View.GONE
            binding.detailBtnWishList.visibility = View.GONE
        }

        travel.contentId?.let { contentId ->
            val contentTypeId = travel.contentTypeId ?: 0

            viewLifecycleOwner.lifecycleScope.launch {
                val detailContentInfo =
                    travelRepository.getTravelDetailInfo(contentId, contentTypeId)
                binding.detailTvContent.text = detailContentInfo
            }
        }





        binding.detailBtnWishList.setOnClickListener {
            val mainFragment = MainFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val bundle = Bundle()
            bundle.putInt("initPosition", 4)
            mainFragment.arguments = bundle

            fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
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
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .add(R.id.main_frame, setupFragment)
                .addToBackStack(null)
                .commit()

        }



        viewLifecycleOwner.lifecycleScope.launch {
            val bookmarkList = bookmarkRepository.getBookmarkList()
            val travelToBookmark = travel

            val isBookmarked: Boolean? = bookmarkList?.any { it.title == travelToBookmark.title }

            if (isBookmarked == true) {
                binding.detailBtnBookmark.setImageResource(R.drawable.ic_bookmark_blue)
            } else {
                binding.detailBtnBookmark.setImageResource(R.drawable.ic_bookmark_white)
            }

            binding.detailBtnBookmark.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val bookmarkList = bookmarkRepository.getBookmarkList()
                    val travelToBookmark = travel
                    val isBookmarked: Boolean? = bookmarkList?.any { it.title == travelToBookmark.title }
                    if (isBookmarked == true) {
                        bookmarkRepository.deleteBookmark(travelToBookmark)
                        Toast.makeText(requireContext(), "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.detailBtnBookmark.setImageResource(R.drawable.ic_bookmark_white)
                    } else {
                        bookmarkRepository.addBookmark(travelToBookmark)
                        Toast.makeText(requireContext(), "북마크가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.detailBtnBookmark.setImageResource(R.drawable.ic_bookmark_blue)
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment


        mapFragment
            .mapView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.detailFrameScrollView.requestDisallowInterceptTouchEvent(true)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        binding.detailFrameScrollView.requestDisallowInterceptTouchEvent(false)
                    }
                }
                false
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(travel: Travel, menuVisible: Boolean): DetailFragment {
            return DetailFragment(travel, menuVisible)
        }
    }
}
