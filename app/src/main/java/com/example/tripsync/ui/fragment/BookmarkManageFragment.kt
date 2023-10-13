package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentBookmarkManageBinding
import com.example.tripsync.ui.adapter.BookmarkManageAdapter
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory

class BookmarkManageFragment : Fragment() {

    private var _binding: FragmentBookmarkManageBinding? = null
    private val binding: FragmentBookmarkManageBinding
        get() = _binding!!

    private val viewModel: BookmarkManageViewModel by viewModels { BookmarkManageViewModelFactory() }

    private val adapter by lazy {
        BookmarkManageAdapter {
            viewModel.deleteBookmarkList(it)
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

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): BookmarkManageFragment {
            return BookmarkManageFragment()
        }
    }

}