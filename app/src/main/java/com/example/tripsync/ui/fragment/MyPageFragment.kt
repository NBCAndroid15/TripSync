package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentMyPageBinding
import com.example.tripsync.viewmodel.MyPageViewModel
import com.example.tripsync.viewmodel.MyPageViewModelFactory

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels { MyPageViewModelFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        //doSomething
    }

    private fun initView() {
        binding.mypageLogoutBtn.setOnClickListener {
            viewModel.logout()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, LoginFragment.newInstance())
                .commit()
        }
        binding.mypageProfileEmail.text = viewModel.curUser?.email ?: "unknown"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }
}