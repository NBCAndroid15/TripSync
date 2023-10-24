package com.example.tripsync.ui.fragment.plan

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentNaverMapBinding
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay


class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()
    private val line = PolylineOverlay()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private var _binding: FragmentNaverMapBinding? = null
    private val binding: FragmentNaverMapBinding
        get() = _binding!!


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
        this.naverMap = p0

        sharedViewModel.planItems.observe(viewLifecycleOwner, Observer { locations ->
            Log.d("map", "Selected locations: $locations")
            // 지도 초기화
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
                    Log.d("map", "Selected locations: ${location.mapX}, ${location.mapY}")

                    val cameraPosition = CameraPosition(LatLng(location.mapY, location.mapX), 10.0)
                    naverMap.cameraPosition = cameraPosition
                }
            }
            // 마커 동선
            if (lineLatLng.size >= 2) {
                line.coords = lineLatLng
                line.color = Color.GRAY
                line.width = 3
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
        fun newInstance(): NaverMapFragment {
            return NaverMapFragment()
        }
    }

}