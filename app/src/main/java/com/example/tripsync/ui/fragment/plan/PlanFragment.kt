package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBookmarkListAdapter
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBoomarkListDialog
import com.example.tripsync.ui.fragment.plan.plansearchlist.PlanSearchListDialog
import com.example.tripsync.ui.fragment.setup.setupuseradd.SetupUserAddDialog
import com.example.tripsync.ui.fragment.setup.PlanMemoDialog
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import org.json.JSONArray
import org.json.JSONObject

class PlanFragment : Fragment() {


    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter : PlanListAdapter
    private lateinit var userAdapter : PlanUserNameAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var bookmarkAdapter : PlanBookmarkListAdapter




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

        binding.planCheckBtn.setOnClickListener {
            // 전 페이지로 이동
            sharedViewModel.updatePlan()
            requireActivity().onBackPressed()
        }


        getTitleOrDate()

    }

    private fun initViewModel() {

        with(sharedViewModel) {
            planItems.observe(viewLifecycleOwner, Observer { planItems ->
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

        planCallBtn.setOnClickListener {
            val fragment = PlanBoomarkListDialog()
            fragment.show(parentFragmentManager, "bookmarkListDialog")
        }

        planSearchBtn.setOnClickListener {
            val fragment = PlanSearchListDialog()
            fragment.show(parentFragmentManager, "searchListDialog")
        }

        binding.planTextView.text = sharedViewModel._plan.planDetailList?.get(sharedViewModel.currentPosition)?.content

    }

    private fun showMemoDialog() = with(binding) {

        val dialogFragment = PlanMemoDialog(requireContext())
        dialogFragment.setOnSaveListener { memoText ->
           memoText?.let {
               if(memoText.isNotBlank()) {
                   sharedViewModel.addMemo(memoText)
                   planTextView.text = memoText

               }
           }
        }
        dialogFragment.show()

    }

    private fun getTitleOrDate () = with(binding) {
        val plan = sharedViewModel._plan

        // null 체크
        if (plan != null && sharedViewModel.currentPosition >= 0) {
            planTextTitle.text = plan.title
            if (plan.planDetailList != null && sharedViewModel.currentPosition < plan.planDetailList!!.size) {
                planDate.text = plan.planDetailList!![sharedViewModel.currentPosition]?.date

            }

        }

    }

    private fun deletePlanItem(item: Travel) {
        sharedViewModel.planRemoveItem(item)
    }


}