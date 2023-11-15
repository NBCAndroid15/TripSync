package com.trip.tripsync.ui.fragment.setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trip.tripsync.databinding.FragmentSetupGuideBinding

class SetupGuideFragment : Fragment() {

    private var _binding: FragmentSetupGuideBinding? = null
    private val binding: FragmentSetupGuideBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupGuideBinding.inflate(inflater, container, false)

        binding.guideCloseBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@SetupGuideFragment).commit()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(): SetupGuideFragment {
            return SetupGuideFragment()
        }
    }
}