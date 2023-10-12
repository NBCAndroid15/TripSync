package com.example.tripsync.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.databinding.ActivityMainBinding
import com.example.tripsync.ui.fragment.LoginFragment
import com.example.tripsync.ui.fragment.PlanFragment
import com.example.tripsync.ui.fragment.SetupFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, SetupFragment.newInstance())
            .commit()

        lifecycleScope.launch {
            val travelRepositoryImpl = TravelRepositoryImpl()

            val travelList1 = travelRepositoryImpl.getTravelInfoWithPosition(1, 126.9885339952, 37.5665544247)
            Log.d("getTravelInfoWithPosition", travelList1.toString())

            val travelList2 = travelRepositoryImpl.getTravelInfo(1, "서울")
            Log.d("getTravelInfo", travelList2.toString())

            val travelList3 = travelRepositoryImpl.getFestivalInfo(1)
            Log.d("getFestivalInfo", travelList3.toString())
        }

        lifecycleScope.launch {
            // 실패하면 null 반환
            val repo = AuthRepositoryImpl()
            repo.register("test@abc.com", "abcd1234")
            val result = repo.login("test@abc.com", "abcd1234")
            Log.d("fbuser", result?.user.toString())
        }

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











