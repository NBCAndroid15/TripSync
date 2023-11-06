package com.example.tripsync.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.databinding.ActivityMainBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.ui.fragment.CommunityWriteFragment
import com.example.tripsync.ui.fragment.LoginFragment
import com.example.tripsync.ui.fragment.plan.PlanFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

//    private val permissions = arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if(!hasPermission()) {
//            ActivityCompat.requestPermissions(this, permissions,
//                PERMISSION_REQUEST_CODE
//            )
//        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, PlanFragment.newInstance())
            .commit()
        /*
        lifecycleScope.launch {
            val travelRepositoryImpl = TravelRepositoryImpl()

            val travelList1 = travelRepositoryImpl.getTravelInfoWithPosition(1, 126.9885339952, 37.5665544247)
            Log.d("getTravelInfoWithPosition", travelList1.toString())

            val travelList2 = travelRepositoryImpl.getTravelInfo(1, "서울")
            Log.d("getTravelInfo", travelList2.toString())

            val travelList3 = travelRepositoryImpl.getFestivalInfo(1)
            Log.d("getFestivalInfo", travelList3.toString())

            val repo = AuthRepositoryImpl()
            repo.register("test@abc.com", "abcd1234")
            val result = repo.login("test@abc.com", "abcd1234")
            Log.d("fbuser", result?.user.toString())

            val bookmarkRepositoryImpl = BookmarkRepositoryImpl()

            travelList2.forEach {
                bookmarkRepositoryImpl.addBookmark(it)
            }
detail_btn_back
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

//    private fun hasPermission() : Boolean {
//        for (permission in permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission)
//                != PackageManager.PERMISSION_GRANTED) {
//                return false
//            }
//        }
//        return true
//    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }


}











