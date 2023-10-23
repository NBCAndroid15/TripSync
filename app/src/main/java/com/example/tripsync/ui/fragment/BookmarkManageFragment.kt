package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentBookmarkManageBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.adapter.BookmarkAreaAdapter
import com.example.tripsync.ui.adapter.BookmarkManageAdapter
import com.example.tripsync.ui.dialog.ConfirmDialog
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory

class BookmarkManageFragment : Fragment() {

    private lateinit var bookmarkAreaAdapter: BookmarkAreaAdapter
    private var _binding: FragmentBookmarkManageBinding? = null
    private val binding: FragmentBookmarkManageBinding
        get() = _binding!!

    private val viewModel: BookmarkManageViewModel by viewModels { BookmarkManageViewModelFactory() }

    private val adapter by lazy {
        BookmarkManageAdapter {
            ConfirmDialog.newInstance(it).let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "ConfirmDialog")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.bookmarkManageBookmarkRv.adapter = adapter
        binding.bookmarkManageBookmarkRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.bookmarkManageBookmarkRv.itemAnimator = null

        // 지역
        val areaList = mutableListOf("전체", "서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종", "경기", "강원", "충북", "충남", "경북", "경남", "전북", "전남", "제주")
        bookmarkAreaAdapter = BookmarkAreaAdapter(areaList)
        binding.bookmarkCategoryRv.adapter = bookmarkAreaAdapter
        binding.bookmarkCategoryRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        bookmarkAreaAdapter.itemClick = object : BookmarkAreaAdapter.ItemClick {
            override fun onClick(keyword: String) {
                //travelViewModel.updateKeyword(keyword)
            }
        }

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

        setFragmentResultListener("deleteConfirm") { _, bundle ->
            if (bundle.getBoolean("isConfirm")) {
                viewModel.deleteBookmarkList(bundle.getParcelable("travel") ?: Travel())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener("deleteConfirm")
        _binding = null
    }

    companion object {
        fun newInstance(): BookmarkManageFragment {
            return BookmarkManageFragment()
        }
    }

}