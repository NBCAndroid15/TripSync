package com.trip.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trip.tripsync.data.CommunityRepositoryImpl
import com.trip.tripsync.databinding.FragmentCommunityWriteBinding
import com.trip.tripsync.model.ContentPost
import com.trip.tripsync.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommunityWriteFragment : Fragment() {

    private val communityRepository = CommunityRepositoryImpl()
    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding: FragmentCommunityWriteBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.communityPostButton.setOnClickListener {
            val post = Post (
                title = "여행지 추천해드려요",
                timeStamp = System.currentTimeMillis(),
                postType = "여행",
                        content =
                listOf(
                    ContentPost(
                        memoNo = "안녕안녕",
                        imageUrl = listOf("dsadsa", "dasda", "asdadsa"),
                    )
                )

            )

            GlobalScope.launch(Dispatchers.IO) {
                communityRepository.addPost(post)
            }
        }

        binding.communityBackButton.setOnClickListener {
            val post = Post(
                title = "여행지 추천해드려요"
            )

            GlobalScope.launch(Dispatchers.IO) {
                communityRepository.removePost(post)
            }
        }
    }



    companion object {
        fun newInstance(): CommunityWriteFragment {
            return CommunityWriteFragment()
        }
    }
}