package com.example.tripsync.ui.fragment.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
                    // 검색어가 비어있지 않은 경우 검색 수행
                    insearch(searchText)

                    // 키보드 숨김
                    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)
                    Toast.makeText(requireContext(), "검색했어요!", Toast.LENGTH_SHORT).show()
                } else {
                    // 검색어가 비어있을 때 토스트 메시지 표시
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

                val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)
                Toast.makeText(requireContext(), "검색했어요!", Toast.LENGTH_SHORT).show()
            }  else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun insearch(query: String) {
        var travelRepository = TravelRepositoryImpl()
        lifecycleScope.launch {
            var travelList = travelRepository.getTravelInfo(1, query)
            adapter.setList(travelList)
        }
    }

    override fun onItemClick(travel: Travel) {
        val fragment = DetailFragment(travel)
        requireActivity().supportFragmentManager.beginTransaction()
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



