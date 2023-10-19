package com.example.tripsync.ui.fragment.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
                insearch(binding.searchEtSearchbar.text.toString())
                return@setOnKeyListener true
                val imm =
                    context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchEtSearchbar.windowToken, 0)

            }
            return@setOnKeyListener false
        }

        binding.searchBtn.setOnClickListener {
            insearch(binding.searchEtSearchbar.text.toString())
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



