package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tripsync.R
import com.example.tripsync.data.RetrofitInstance.api
import com.example.tripsync.databinding.FragmentDetailBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.search.SearchAdapter
import com.example.tripsync.ui.fragment.search.SearchFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment(private val travel: Travel) : Fragment() {

    private var _binding: FragmentDetailBinding? = null
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


    }

    private suspend fun getTravelDetailInfo(contentId: Int, contentTypeId: Int): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getDetailInfo(contentId = contentId, contentTypeId = contentTypeId)

                if (response.isSuccessful) {
                    val data = response.body()?.response?.body?.items?.item

                    if (data.isNullOrEmpty()) {
                        ""
                    } else {
                        data[0].overview
                    }
                } else {
                    ""
                }
            } catch (e: Exception) {
                ""
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val contentId = travel.contentId!!
            val contentTypeId = travel.contentTypeId!!


            val detailInfo = getTravelDetailInfo(contentId, contentTypeId)


            binding.detailTvContent.text = detailInfo
        }
        bind(travel)

        binding.detailBtnBack.setOnClickListener {
            val searchFragment = SearchFragment() // SearchFragment의 인스턴스 생성
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, searchFragment) // R.id.fragment_container는 Fragment를 표시하는 레이아웃의 ID입니다.
                .commit()
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
