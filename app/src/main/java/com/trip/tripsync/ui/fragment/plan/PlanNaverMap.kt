package com.trip.tripsync.ui.fragment.plan

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentNaverMapBinding
import com.trip.tripsync.ui.fragment.setup.NaverMapFragment
import com.trip.tripsync.viewmodel.SharedViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay


class PlanNaverMap : Fragment(), OnMapReadyCallback {

    lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()
    private val line = PolylineOverlay()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private var _binding: FragmentNaverMapBinding? = null
    private val binding: FragmentNaverMapBinding
        get() = _binding!!
    private var activeMarker: Marker? = null

    private var markerImages: List<Int> = listOf(
        R.drawable.map_1,
        R.drawable.map_2,
        R.drawable.map_3,
        R.drawable.map_4,
        R.drawable.map_5,
        R.drawable.map_6,
        R.drawable.map_7,
        R.drawable.map_8,
        R.drawable.map_69,
        R.drawable.map_10
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNaverMapBinding.inflate(inflater, container, false)
        mapView = binding.mapContainer
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initView()

        return binding.root
    }


    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0

        sharedViewModel.planItems.observe(viewLifecycleOwner, Observer { locations ->
            Log.d("PlanNaverMap", "Locations updated: $locations")

            // 지도 초기화
            markers.forEach { it.map = null }
            markers.clear()

            val lineLatLng = mutableListOf<LatLng>()

            locations?.forEachIndexed { index, location ->
                if (location.mapY != null && location.mapX != null) {
                    val marker = Marker()
                    marker.position = LatLng(location.mapY, location.mapX)

                    lineLatLng.add(LatLng(location.mapY, location.mapX))

                    val markerImage = getMarkerImage(index)
                    marker.icon = OverlayImage.fromResource(markerImage)

                    marker.map = naverMap
                    marker.alpha = 0.8f
                    naverMap.minZoom = 5.0
                    markers.add(marker)

                    val cameraPosition = CameraPosition(LatLng(location.mapY, location.mapX), 9.0)
                    naverMap.cameraPosition = cameraPosition


                }
            }
            // 마커 동선
            if (lineLatLng.size >= 2) {
                line.coords = lineLatLng
                line.color = Color.GRAY
                line.width = 3
                line.setPattern(10, 5)
                line.joinType = PolylineOverlay.LineJoin.Round
                line.map = naverMap
            } else {
                line.map = null
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapView.onDestroy()
    }
    companion object {
        fun newInstance(): PlanNaverMap {
            return PlanNaverMap()
        }
    }

    private fun initView() {
        binding.naverMapBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, NaverMapFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    private fun getMarkerImage(index: Int) : Int {
        return if (index < markerImages.size) {
            markerImages[index]
        } else {
            R.drawable.map_1
        }
    }

}