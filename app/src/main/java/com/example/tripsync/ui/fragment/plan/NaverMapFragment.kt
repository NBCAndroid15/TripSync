package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.tripsync.databinding.FragmentNaverMapBinding
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker


class NaverMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()
    private val sharedViewModel: SharedViewModel by activityViewModels()


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
        this.naverMap = p0
        sharedViewModel.selectedLocation.observe(viewLifecycleOwner, Observer { locations ->
            Log.d("map", "Selected locations: $locations")

//            markers.forEach { it.map = null }
//            markers.clear()


            locations.forEach { location ->
                if (location.mapX != null && location.mapY != null) {
                    val marker = Marker()
                    marker.position = LatLng(location.mapX, location.mapY)
                    marker.map = naverMap
                    naverMap.minZoom = 5.0
                    markers.add(marker)
                    Log.d("map", "Selected locations: ${location.mapX}, ${location.mapY}")

//                    val cameraPosition = CameraPosition(LatLng(location.mapX, location.mapY), 10.0)
//                    naverMap.cameraPosition = cameraPosition
                }
            }
        })
//        observeSelectedLocation()


    }

    private fun observeSelectedLocation() {
        sharedViewModel.selectedLocation.observe(viewLifecycleOwner, Observer { locations ->
            Log.d("map", "Selected locations: $locations")

//            markers.forEach { it.map = null }
//            markers.clear()

            for (location in locations) {
                if (location.mapX != null && location.mapY != null) {
                    val marker = Marker()
                    marker.position = LatLng(location.mapX, location.mapY)
                    marker.map = naverMap
//                    markers.add(marker)
                    Log.d("map", "Selected locations: ${location.mapX}, ${location.mapY}")


//                    val cameraPosition = CameraPosition(LatLng(location.mapX, location.mapY), 10.0)
//                    naverMap.cameraPosition = cameraPosition
                }
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