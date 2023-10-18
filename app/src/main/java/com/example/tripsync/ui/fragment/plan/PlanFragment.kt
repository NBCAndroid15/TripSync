package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBoomarkListFragment
import com.example.tripsync.ui.fragment.plan.plansearchlist.PlanSearchListFragment
import com.example.tripsync.ui.fragment.setup.PlanMemoFragment
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory

class PlanFragment : Fragment() {


    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlanListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        recyclerView = binding.planRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlanListAdapter { position, travel ->
        }
        recyclerView.adapter = adapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

        binding.planEditBtn.setOnClickListener {
            showMemoDialog()
        }

        getTitleOrDate()

    }

    private fun initViewModel() {
        with(sharedViewModel){
            planBookItem.observe(viewLifecycleOwner, Observer {

                if(it != null) {
                    adapter.submitList(it)
                }
            })

            planSearchItem.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlanFragment {
            return PlanFragment()
        }
    }

    private fun initView() = with(binding) {

        binding.planCallBtn.setOnClickListener {
            val fragment = PlanBoomarkListFragment()
            fragment.show(parentFragmentManager, "bookmarkListDialog")
        }

        binding.planSearchBtn.setOnClickListener {
            val fragment = PlanSearchListFragment()
            fragment.show(parentFragmentManager, "searchListDialog")
        }
    }

    private fun showMemoDialog() = with(binding) {
        val dialogFragment = PlanMemoFragment(requireContext())
        dialogFragment.setOnSaveListener { memoText ->
            planTextView.text = memoText
            planEditBtn.visibility = View.GONE
        }
        dialogFragment.show()

    }

    private fun getTitleOrDate () = with(binding) {

        sharedViewModel.sharedTitle.observe(viewLifecycleOwner, Observer {
            planTextTitle.text = it
        })

        sharedViewModel.sharedDate.observe(viewLifecycleOwner, Observer { date ->
            if(date.isNotEmpty()) {
                val dateText = date.joinToString { "${it.year}년 ${it.month}월 ${it.day}일"}
                binding.planDate.text = dateText
            }
        })


    }

}