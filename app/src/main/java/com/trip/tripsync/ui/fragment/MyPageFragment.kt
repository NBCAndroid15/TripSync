package com.trip.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentMyPageBinding
import com.trip.tripsync.ui.adapter.MyPageViewPagerAdapter
import com.trip.tripsync.ui.dialog.UserManageFragment
import com.trip.tripsync.viewmodel.MyPageViewModel
import com.trip.tripsync.viewmodel.MyPageViewModelFactory
import com.trip.tripsync.viewmodel.UserProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = _binding!!

    private val title = arrayOf("북마크 리스트", "친구 목록")

    private val viewModel: MyPageViewModel by activityViewModels { MyPageViewModelFactory() }
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        binding.mypageProfileBg.clipToOutline = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        userProfileViewModel.curUser.observe(viewLifecycleOwner, Observer { user ->
            Glide.with(this)
                .load(user.profileImg)
                .error(R.drawable.defalt_profile)
                .into(binding.mypageProfileBg)
        })
        //doSomething
    }

    private fun initView() {
        userProfileViewModel.getCurrentUserSnapshot()
        binding.mypageViewPager.isUserInputEnabled = false
        binding.mypageViewPager.adapter = MyPageViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.mypageTabLayout, binding.mypageViewPager) { tab, position ->
            tab.text = title[position]
        }.attach()

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val currentUserUid = auth.currentUser?.uid



        // 현재 유저 닉네임 불러오기
        if (currentUserUid != null) {
            usersRef.document(currentUserUid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nickname = documentSnapshot.getString("nickname")
                        binding.mypageProfileNickname.text = nickname
                    }
                }
        }

        // 로그아웃
        binding.mypageLogoutBtn.setOnClickListener {
            viewModel.logout()

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, LoginFragment.newInstance())
                .commit()
        }

        // 회원정보 수정
        binding.mypageConfigBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.main_frame, UserManageFragment())
                .addToBackStack(null)
                .commit()
            }
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