package com.example.tripsync.ui.fragment.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.databinding.FragmentSearchBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.DetailFragment
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), SearchAdapter.OnItemClick {

    private val adapter = SearchAdapter()
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!
    private var isLoading = false
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        searchResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.searchRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener (this)

        val areaKeywords = listOf("서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종특별자치시", "경기도", "강원특별자치도", "충청북도", "충청남도", "경상북도", "경상남도", "전라북도", "전라남도", "제주도", "전체")
        val autoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, areaKeywords)
        binding.searchEtSearchbar.setAdapter(autoAdapter)

        binding.searchEtSearchbar.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                searchResult()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchEtSearchbar.setOnItemClickListener { parent, view, position, id ->
            searchResult()
        }

        binding.searchBtn.setOnClickListener {
            searchResult()
            if (binding.searchEtSearchbar.text.isEmpty()) {
                Toast.makeText(requireContext(), "검색어를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // 스크롤이 끝까지 도달하면 더 많은 데이터를 로드
                    loadMoreData()
                }
            }
        })
    }

    private fun searchResult() {
        val searchText = binding.searchEtSearchbar.text.toString()
        if (searchText.isNotEmpty()) {
            insearch(searchText)
            requireView().post {
                binding.searchIvChar.visibility = View.GONE
                binding.searchIvChar2.visibility = View.GONE
                binding.searchProgress.visibility = View.VISIBLE
            }
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)
        }
    }

    private fun insearch(query: String) {
        val travelRepository = TravelRepositoryImpl()
        lifecycleScope.launch {
            val travelList = travelRepository.getTravelInfo(1, query)
            adapter.setList(travelList)
            binding.searchProgress.visibility = View.GONE
        }
    }

    private fun loadMoreData() {
        isLoading = true
        currentPage++

        val travelRepository = TravelRepositoryImpl()
        lifecycleScope.launch {
            val travelList = travelRepository.getTravelInfo(currentPage, binding.searchEtSearchbar.text.toString())
            adapter.addItems(travelList)
            isLoading = false
        }
    }

    override fun onItemClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

}



