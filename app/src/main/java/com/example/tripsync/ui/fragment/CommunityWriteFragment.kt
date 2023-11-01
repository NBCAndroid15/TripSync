package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentCommunityWriteBinding
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.createBundle

class CommunityWriteFragment : Fragment() {

    private var _binding: FragmentCommunityWriteBinding? = null
    private val binding: FragmentCommunityWriteBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityWriteBinding.inflate(inflater, container, false)

        // 뒤로가기 버튼 클릭 -> (지금은 임시)
        binding.communityBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, CommunityContentFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.communityPostButton.setOnClickListener {
            val communityTitle = binding.communityTitleText.text.toString()
            val communityContent = binding.communityContentText.text.toString()
            var category: String? = null

            if (binding.communityCategoryLifeButton.isPressed) {
                category = "life"
            } else if (binding.communityCategoryReviewButton.isPressed) {
                category = "review"
            } else if (binding.communityCategoryQuestionButton.isPressed) {
                category = "question"
            }
            val bundle = createBundle(communityTitle, communityContent, category)
            navigateBundle(bundle)
        }
        return binding.root
    }

    private fun createBundle(title: String, content: String, category: String?): Bundle {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("content", content)

        if (category != null) {
            bundle.putString(category, category)
        }

        return bundle
    }

    private fun navigateBundle(bundle: Bundle) {
        val communityContentFragment = CommunityContentFragment()
        communityContentFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, communityContentFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CommunityWriteFragment {
            return CommunityWriteFragment()
        }
    }
}