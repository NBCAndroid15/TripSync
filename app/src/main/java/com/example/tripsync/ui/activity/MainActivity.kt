package com.example.tripsync.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.data.BookmarkRepositoryImpl
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.databinding.ActivityMainBinding
import com.example.tripsync.model.Bookmark
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.ui.fragment.LoginFragment
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_frame, LoginFragment.newInstance())
            .commit()

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











