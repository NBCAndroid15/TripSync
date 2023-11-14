package com.trip.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trip.tripsync.databinding.FragmentHomeGuideBinding

class HomeGuideFragment : Fragment() {

    private var _binding: FragmentHomeGuideBinding? = null
    private val binding: FragmentHomeGuideBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeGuideBinding.inflate(inflater, container, false)

        binding.guideCloseBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@HomeGuideFragment).commit()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(): HomeGuideFragment {
            return HomeGuideFragment()
        }
    }
}