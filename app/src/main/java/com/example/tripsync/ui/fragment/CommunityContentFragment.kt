package com.example.tripsync.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentCoinBinding
import com.example.tripsync.databinding.FragmentCommunityContentBinding
import com.example.tripsync.databinding.FragmentCommunityWriteBinding

class CommunityContentFragment : Fragment() {

    private var _binding: FragmentCommunityContentBinding? = null
    private val binding: FragmentCommunityContentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityContentBinding.inflate(inflater, container, false)

        // 뒤로가기 버튼 클릭 -> (지금은 임시)
        binding.communityBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, CommunityWriteFragment())
                .addToBackStack(null)
                .commit()
        }

        val communityTitle = arguments?.getString("title")
        val communityContent = arguments?.getString("content")
        val categoryLife = arguments?.getString("life")
        val categoryReview = arguments?.getString("review")
        val categoryQuestion = arguments?.getString("question")

        binding.communityTitleText.text = communityTitle
        binding.communityContentText.text = communityContent

        when {
            categoryLife != null -> {
                binding.communityCategoryText.text = categoryLife
            }
            categoryReview != null -> {
                binding.communityCategoryText.text = categoryReview
            }
            categoryQuestion != null -> {
                binding.communityCategoryText.text = categoryQuestion
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CommunityContentFragment {
            return CommunityContentFragment()
        }
    }
}