package com.trip.tripsync.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trip.tripsync.databinding.FragmentPlanGuideBinding

class PlanGuideFragment : Fragment() {

    private var _binding: FragmentPlanGuideBinding? = null
    private val binding: FragmentPlanGuideBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanGuideBinding.inflate(inflater, container, false)

        binding.guideCloseBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@PlanGuideFragment).commit()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(): PlanGuideFragment {
            return PlanGuideFragment()
        }
    }
}