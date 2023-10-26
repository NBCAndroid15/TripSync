package com.example.tripsync.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentMyPageBinding
import com.example.tripsync.ui.adapter.MyPageViewPagerAdapter
import com.example.tripsync.ui.dialog.UserManageDialog
import com.example.tripsync.viewmodel.MyPageViewModel
import com.example.tripsync.viewmodel.MyPageViewModelFactory
import com.example.tripsync.viewmodel.UserProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentMyPageBinding? = null
    private val binding: FragmentMyPageBinding
        get() = _binding!!

    private val title = arrayOf("북마크 리스트", "친구 목록")

    private val viewModel: MyPageViewModel by viewModels { MyPageViewModelFactory() }
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

        userProfileViewModel.loadImageUrlFromDatabase()

        userProfileViewModel.getImageUrl().observe(viewLifecycleOwner, Observer { imageUrl ->
            Log.d("이미지url", imageUrl)
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.mypageProfileBg)
        })
        //doSomething
    }

    private fun initView() {
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
                .replace(R.id.main_frame, UserManageDialog())
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