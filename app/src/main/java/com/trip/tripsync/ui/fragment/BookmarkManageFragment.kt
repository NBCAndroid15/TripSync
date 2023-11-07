package com.trip.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentBookmarkManageBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.adapter.BookmarkAreaAdapter
import com.trip.tripsync.ui.adapter.BookmarkManageAdapter
import com.trip.tripsync.ui.dialog.ConfirmDialog
import com.trip.tripsync.viewmodel.BookmarkManageViewModel
import com.trip.tripsync.viewmodel.BookmarkManageViewModelFactory

class BookmarkManageFragment : Fragment() {

    private lateinit var bookmarkAreaAdapter: BookmarkAreaAdapter
    private var _binding: FragmentBookmarkManageBinding? = null
    private val binding: FragmentBookmarkManageBinding
        get() = _binding!!

    private val viewModel: BookmarkManageViewModel by activityViewModels { BookmarkManageViewModelFactory() }


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

    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkList()
    }

    private fun initView() {
        binding.bookmarkManageBookmarkRv.adapter = BookmarkManageAdapter ({
            ConfirmDialog.newInstance(it).let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "ConfirmDialog")
            }
        }, { travel ->
            val fragment = DetailFragment(travel)
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
                    R.anim.exit_to_right)
                .add(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit()
        })
        binding.bookmarkManageBookmarkRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.bookmarkManageBookmarkRv.itemAnimator = null

        // 지역
        val areaList = mutableListOf("전체", "서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종", "경기", "강원", "충북", "충남", "경북", "경남", "전북", "전남", "제주")
        bookmarkAreaAdapter = BookmarkAreaAdapter(areaList)
        binding.bookmarkCategoryRv.adapter = bookmarkAreaAdapter
        bookmarkAreaAdapter.itemStates[0] = true
        binding.bookmarkCategoryRv.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        bookmarkAreaAdapter.itemClick = object : BookmarkAreaAdapter.ItemClick {
            override fun onClick(keyword: String) {
                viewModel.getBookmarkListWithFilter(keyword)
            }
        }

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                (binding.bookmarkManageBookmarkRv.adapter as BookmarkManageAdapter).setList(it)
                binding.bookmarkManageHintText.visibility = View.VISIBLE
                binding.bookmarkManageBookmarkRv.visibility = View.GONE
            } else {
                binding.bookmarkManageHintText.visibility = View.GONE
                binding.bookmarkManageBookmarkRv.visibility = View.VISIBLE
                (binding.bookmarkManageBookmarkRv.adapter as BookmarkManageAdapter).setList(it)
            }
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