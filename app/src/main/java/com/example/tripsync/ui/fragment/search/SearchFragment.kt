package com.example.tripsync.ui.fragment.search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import kotlin.concurrent.thread

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView4

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener (this)




        binding.searchEtSearchbar.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                val searchText = binding.searchEtSearchbar.text.toString()

                if (searchText.isNotEmpty()) {
                    insearch(searchText)

                    binding.searchIvChar.visibility = View.GONE
                    binding.searchIvChar2.visibility = View.GONE
                    binding.searchProgress.visibility = View.VISIBLE


                    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)
                    Toast.makeText(requireContext(), "검색했어요!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "검색어를 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchBtn.setOnClickListener {
            val searchText = binding.searchEtSearchbar.text.toString()
            if (searchText.isNotEmpty()) {
            insearch(binding.searchEtSearchbar.text.toString())

                binding.searchIvChar.visibility = View.GONE
                binding.searchIvChar2.visibility = View.GONE
                binding.searchProgress.visibility = View.VISIBLE


                val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)
                Toast.makeText(requireContext(), "검색했어요!", Toast.LENGTH_SHORT).show()
            }  else {
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



