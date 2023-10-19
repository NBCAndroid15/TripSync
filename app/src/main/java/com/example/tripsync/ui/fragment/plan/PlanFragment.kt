package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBoomarkListDialog
import com.example.tripsync.ui.fragment.plan.plansearchlist.PlanSearchListDialog
import com.example.tripsync.ui.fragment.setup.setupuseradd.SetupUserAddDialog
import com.example.tripsync.ui.fragment.setup.PlanMemoDialog
import com.example.tripsync.ui.fragment.setup.SharedViewModel

class PlanFragment : Fragment() {


    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter : PlanListAdapter
    private lateinit var userAdapter : PlanUserNameAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        recyclerView = binding.planRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlanListAdapter {
            deletePlanItem(it)
        }

        recyclerView.adapter = adapter

        val swipeToDeleteCallback = SwipeToDeleteCallback(adapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4 )
        }
        itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.planRecycler)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initUserName()


        binding.planEditBtn.setOnClickListener {
            showMemoDialog()
        }

        getTitleOrDate()

    }

    private fun initViewModel() {

        with(sharedViewModel) {
            planItems.observe(viewLifecycleOwner, Observer { planItems ->
                val oldItemSize = adapter.currentList.size
                adapter.submitList(null)
                adapter.submitList(planItems)
                itemTouchHelper.attachToRecyclerView(null)
                itemTouchHelper.attachToRecyclerView(binding.planRecycler)
                adapter.notifyDataSetChanged()
            })

        }
    }

    private fun initUserName()=with(binding) {
        userAdapter = PlanUserNameAdapter()
        planUsernameRecycler.adapter = userAdapter
        planUsernameRecycler.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        sharedViewModel.userNickName.observe(viewLifecycleOwner, Observer { name ->
            userAdapter.submitList(name)
        })
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
            val fragment = PlanBoomarkListDialog()
            fragment.show(parentFragmentManager, "bookmarkListDialog")
        }

        binding.planSearchBtn.setOnClickListener {
            val fragment = PlanSearchListDialog()
            fragment.show(parentFragmentManager, "searchListDialog")
        }
    }

    private fun showMemoDialog() = with(binding) {
        val dialogFragment = PlanMemoDialog(requireContext())
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

    private fun deletePlanItem(item: TestModel) {
        sharedViewModel.planRemoveItem(item)

    }

}