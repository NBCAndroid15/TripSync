package com.trip.tripsync.ui.fragment.plan

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentPlanBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.adapter.plan.ItemDecoration
import com.trip.tripsync.ui.adapter.plan.PlanListAdapter
import com.trip.tripsync.ui.adapter.plan.PlanUserNameAdapter
import com.trip.tripsync.ui.dialog.confirm.ConfirmDialog
import com.trip.tripsync.ui.dialog.plan.PlanBoomarkListDialog
import com.trip.tripsync.ui.dialog.plan.PlanSearchListDialog
import com.trip.tripsync.ui.dialog.plan.PlanMemoDialog
import com.trip.tripsync.viewmodel.SharedViewModel
import com.trip.tripsync.ui.dialog.UserCheckDialogFragment
import com.trip.tripsync.ui.fragment.detail.DetailFragment

class PlanFragment : Fragment(), PlanListAdapter.OnItemClickListener {


    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var recyclerView: RecyclerView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter : PlanListAdapter
    private lateinit var userAdapter : PlanUserNameAdapter

    private lateinit var naverMapFragment: PlanNaverMap
    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        recyclerView = binding.planRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlanListAdapter ( {
            ConfirmDialog.newInstance(it).let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "ConfirmDialog")
            }
        }, {
            sharedViewModel.setPlanItems(it)
        })

        initVisible()

        recyclerView.adapter = adapter
        itemTouchHelper = ItemTouchHelper(PlanSwapManage(adapter, requireContext(), ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter.setOnItemClickListener(this)

        return binding.root
    }

    private fun isFirstRun(fragmentTag: String): Boolean {
        return sharedPreferences.getBoolean("isFirstRun_$fragmentTag", true)
    }
    private fun setFirstRunFlag(fragmentTag: String) {
        sharedPreferences.edit().putBoolean("isFirstRun_$fragmentTag", false).apply()
    }
    private fun showGuideFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, PlanGuideFragment())
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        sharedPreferences = requireContext().getSharedPreferences(
            "com.trip.tripsync.PREFERENCE_FILE_KEY",
            Context.MODE_PRIVATE
        )
        if (isFirstRun("plan")) {
            showGuideFragment()
            setFirstRunFlag("plan")
        }

        initView()
        initViewModel()
        initUserName()

        val initialText = binding.planTextView.text.toString()
        if (initialText.isNotEmpty()) {
            sharedViewModel.setHint(false)
        } else {
            sharedViewModel.setHint(true)
        }

        binding.planUserCheckBtn.setOnClickListener {
            showUserCheckDialog()
        }

        binding.planTextView.setOnClickListener {
            showMemoDialog()
        }

        binding.planCheckBtn.setOnClickListener {
            // 전 페이지로 이동
            sharedViewModel.updatePlan()
            requireActivity().onBackPressed()
        }

        naverMapFragment = childFragmentManager.findFragmentById(R.id.naver_map_fragment) as PlanNaverMap

        naverMapFragment
            .mapView.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.scrollview.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.scrollview.requestDisallowInterceptTouchEvent(false)
                }
            }
                false
        }

        moveToDate()
        getTitleOrDate()

        /*
        * 1. PlanFragment에서 다른 날짜의 프래그먼트로 이동할 수 있음
        * 2. 이동할 때마다 plandate의 텍스트 변경
        * */
        binding.planBefore.setOnClickListener {
            moveToBefore()
            moveToDate()
        }
        binding.planNext.setOnClickListener {
            moveToNext()
            moveToDate()
        }

        moveNextVisible()
        moveBeforeVisible()

    }

    private fun initViewModel() {

        with(sharedViewModel) {
            planItems.observe(viewLifecycleOwner, Observer { planItems ->
                adapter.submitList(null)

                    adapter.submitList(planItems)
                    adapter.notifyDataSetChanged()


            })

        }
    }

    private fun initUserName()=with(binding) {
        userAdapter = PlanUserNameAdapter()
        planUsernameRecycler.adapter = userAdapter
        planUsernameRecycler.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val decoration = ItemDecoration()
        planUsernameRecycler.addItemDecoration(decoration)

        planUsernameRecycler.setOnClickListener {
            UserCheckDialogFragment.newInstance().let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "UserCheckDialog")
            }
        }

        sharedViewModel.userList.observe(viewLifecycleOwner, Observer { name ->
            userAdapter.submitList(name)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFragmentResultListener("deleteConfirm")
        _binding = null
    }

    companion object {
        fun newInstance(): PlanFragment {
            return PlanFragment()
        }
    }

    private fun initView() = with(binding) {

        setFragmentResultListener("deleteConfirm") { _, bundle ->
            if (bundle.getBoolean("isConfirm")) {
                sharedViewModel.planRemoveItem(bundle.getParcelable("travel") ?: Travel())
            }
        }

        planCallBtn.setOnClickListener {
            val fragment = PlanBoomarkListDialog()
            fragment.show(childFragmentManager, "bookmarkListDialog")
        }

        planSearchBtn.setOnClickListener {
            val fragment = PlanSearchListDialog()
            fragment.show(childFragmentManager, "searchListDialog")
        }

        binding.planTextView.text = sharedViewModel._plan.planDetailList?.get(sharedViewModel.currentPosition)?.content

        if (binding.planTextView.text.isNotEmpty()){
            sharedViewModel.setHint(false)
        }
    }

    private fun showUserCheckDialog() {
        UserCheckDialogFragment.newInstance().let { dialog ->
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "UserCheckDialog")
        }
    }

    private fun showMemoDialog() = with(binding) {
        val initialText = planTextView.text.toString()
        val dialogFragment = PlanMemoDialog(requireContext(), initialText)
        dialogFragment.setOnSaveListener { memoText ->
            memoText?.let {
                if (memoText.isNotBlank()) {
                    sharedViewModel.addMemo(memoText)
                    planTextView.text = memoText
                    sharedViewModel.setHint(false)
                } else {
                    sharedViewModel.addMemo("")
                    planTextView.text = ""
                    sharedViewModel.setHint(true)
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
//                planDate.text = plan.planDetailList!![sharedViewModel.currentPosition]?.date

            }

        }

    }

    private fun initVisible () {
        sharedViewModel.ishint.observe(viewLifecycleOwner){isVisible ->
            binding.planTextHint.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    private fun moveToNext () {
        if (sharedViewModel.currentPosition < sharedViewModel._plan.planDetailList?.size?.minus(1) ?: 0) {
            sharedViewModel.initPosition(sharedViewModel.currentPosition + 1)
        }
//        binding.planDate.text = sharedViewModel._plan.planDetailList!![sharedViewModel.currentPosition]?.date
        binding.planTextView.text = sharedViewModel._plan.planDetailList?.get(sharedViewModel.currentPosition)?.content ?: ""
        sharedViewModel.setHint(binding.planTextView.text.isEmpty())

        moveNextVisible()
        moveBeforeVisible()
    }

    private fun moveToBefore() {
        if ( sharedViewModel.currentPosition > 0) {
            sharedViewModel.initPosition(sharedViewModel.currentPosition - 1)
        }
//        binding.planDate.text = sharedViewModel._plan.planDetailList!![sharedViewModel.currentPosition]?.date
        binding.planTextView.text = sharedViewModel._plan.planDetailList?.get(sharedViewModel.currentPosition)?.content ?: ""
        sharedViewModel.setHint(binding.planTextView.text.isEmpty())

        moveBeforeVisible()
        moveNextVisible()
    }

    private fun moveToDate() {
        binding.dateText.text = sharedViewModel._plan.planDetailList!![sharedViewModel.currentPosition]?.date
    }

    private fun moveBeforeVisible() {
        val isFirstDay = sharedViewModel.currentPosition == 0

        binding.planBefore.visibility = if (isFirstDay) View.GONE else View.VISIBLE
    }

    private fun moveNextVisible() {
        val isLastDay = sharedViewModel.currentPosition == (sharedViewModel._plan.planDetailList?.size ?: 0) - 1
        binding.planNext.visibility = if (isLastDay) View.GONE else View.VISIBLE

    }

    override fun onItemClick(item: Travel) {
        val fragment = DetailFragment(item, false)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }


}