package com.example.tripsync.ui.fragment.setup

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.tripsync.R
import com.example.tripsync.databinding.NavermapBinding
import com.example.tripsync.ui.activity.MainActivity.Companion.PERMISSION_REQUEST_CODE
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource


class NaverMapFragment : Fragment(), OnMapReadyCallback {




    private var _binding: NavermapBinding? = null
    private val binding: NavermapBinding
        get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val markers = mutableListOf<Marker>()
    private val line = PolylineOverlay()
    private val sharedViewModel: SharedViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NavermapBinding.inflate(inflater, container, false)
        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSION_REQUEST_CODE)


        return binding.root
    }



    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        p0.locationSource = locationSource

        sharedViewModel.planItems.observe(viewLifecycleOwner, Observer { locations ->
            markers.forEach { it.map = null }
            markers.clear()

            val lineLatLng = mutableListOf<LatLng>()

            locations?.forEachIndexed { index, location ->
                if (location.mapY != null && location.mapX != null) {
                    val marker = Marker()
                    marker.position = LatLng(location.mapY, location.mapX)

                    lineLatLng.add(LatLng(location.mapY, location.mapX))

                    val numberImage = when(index) {
                        0 -> R.drawable.map_1
                        1 -> R.drawable.map_2
                        2 -> R.drawable.map_3
                        3 -> R.drawable.map_4
                        4 -> R.drawable.map_5
                        5 -> R.drawable.map_6
                        6 -> R.drawable.map_7
                        7 -> R.drawable.map_8
                        8 -> R.drawable.map_69
                        9 -> R.drawable.map_10

                        else -> R.drawable.map_2
                    }
                    marker.icon = OverlayImage.fromResource(numberImage)

                    marker.map = naverMap
                    naverMap.minZoom = 5.0
                    markers.add(marker)

                    val cameraPosition = CameraPosition(LatLng(location.mapY, location.mapX), 9.0)
                    naverMap.cameraPosition = cameraPosition
                }
            }
            if (lineLatLng.size >= 2) {
                line.coords = lineLatLng
                line.color = Color.GRAY
                line.width = 3
                line.map = naverMap
            } else {
                line.map = null
            }
        })

        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow



    }

    companion object {
        fun newInstance(): NaverMapFragment {
            return NaverMapFragment()
        }
    }




}