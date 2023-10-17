package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripsync.databinding.FragmentNaverMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    private var _binding: FragmentNaverMapBinding? = null
    private val binding: FragmentNaverMapBinding
        get() = _binding!!

    private val testModel = PlanViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNaverMapBinding.inflate(inflater, container, false)

        mapView = binding.mapContainer
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }


    override fun onMapReady(p0: NaverMap) {

////        for(item in testModel) {
//            val latLng = LatLng(item.mapX!!, item.mapY!!)
//            p0.mapType = NaverMap.MapType.Basic
//            p0.minZoom = 5.0
//
//            val cameraPosition = CameraPosition(latLng, 10.0)
//            p0.cameraPosition = cameraPosition
//
//            val marker = Marker()
//            marker.position = latLng
//            marker.map = p0
////        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapView.onDestroy()
    }

}