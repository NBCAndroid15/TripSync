package com.example.tripsync.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.data.BookmarkRepositoryImpl
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.databinding.ActivityMainBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.ui.fragment.BookmarkManageFragment
import com.example.tripsync.ui.fragment.MyPlanFragment
import com.example.tripsync.ui.fragment.setup.SetupCalendarView
import com.example.tripsync.ui.fragment.setup.SetupFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val repo = AuthRepositoryImpl()
            repo.register("test@abc.com", "abcd1234")
            repo.login("test@abc.com", "abcd1234")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, MyPlanFragment.newInstance())
            .commit()

        /*
        lifecycleScope.launch {
            val travelRepositoryImpl = TravelRepositoryImpl()
            val bookmarkRepositoryImpl = BookmarkRepositoryImpl()
            val travelList = travelRepositoryImpl.getTravelInfo(1, "서울")
            Log.d("getTravelInfo", travelList.toString())


            val testPlan1 = Plan(
                "제주도", listOf(
                    PlanDetail("20231010", "제주도", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231011", "제주도", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231012", "제주도", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231013", "제주도", listOf("친구1", "친구2", "친구3"), "내용", travelList)
                )
            )

            val testPlan2 = Plan(
                "부산", listOf(
                    PlanDetail("20231010", "부산", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231011", "부산", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231012", "부산", listOf("친구1", "친구2", "친구3"), "내용", travelList),
                    PlanDetail("20231013", "부산", listOf("친구1", "친구2", "친구3"), "내용", travelList)
                )
            )

            val planRepositoryImpl = PlanRepositoryImpl()
            planRepositoryImpl.addPlan(testPlan1)
            planRepositoryImpl.addPlan(testPlan2)


        }
        */
        /*
        lifecycleScope.launch {
            // 실패하면 null 반환
            val repo = AuthRepositoryImpl()
            repo.register("test@abc.com", "abcd1234")
            val result = repo.login("test@abc.com", "abcd1234")
            Log.d("fbuser", result?.user.toString())
        }
         */

        /*
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
        lifecycleScope.launch {

            val repo = AuthRepositoryImpl()
            val planRepositoryImpl = PlanRepositoryImpl()
            repo.register("test@abc.com", "abcd1234")
            val result = repo.login("test@abc.com", "abcd1234")
            Log.d("fbuser", result?.user.toString())

            planRepositoryImpl.addPlan(Plan("plan1", listOf(PlanDetail("2022", "title", listOf("friend"), "hi"))))
            planRepositoryImpl.addPlan(Plan("plan2", listOf(PlanDetail("2022", "title", listOf("friend"), "hi"))))
            planRepositoryImpl.addPlan(Plan("plan3", listOf(PlanDetail("2022", "title", listOf("friend"), "hi"))))
        }
        */

    }


}











