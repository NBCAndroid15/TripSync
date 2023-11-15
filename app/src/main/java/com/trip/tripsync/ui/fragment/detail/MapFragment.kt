package com.trip.tripsync.ui.fragment.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.trip.tripsync.databinding.FragmentMapBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.viewmodel.MapViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.trip.tripsync.ui.fragment.detail.DetailFragment

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mapView: MapView
    private lateinit var navermap: NaverMap
    private val viewModel: MapViewModel by viewModels ()
    private var markers = mutableListOf<Marker>()
    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private var currentTravel: Travel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.detailMapContainer
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)



        return binding.root
    }





    override fun onMapReady(naverMap: NaverMap) {

        var detailFragment = parentFragment as DetailFragment
        var mapXY = detailFragment.travel

        var marker = Marker()

        marker.position = LatLng(mapXY.mapY!!, mapXY.mapX!!)
        marker.map = naverMap
        markers.add(marker)

        naverMap.mapType = NaverMap.MapType.Basic
        naverMap.minZoom = 5.0

        val cameraPosition = CameraPosition(LatLng(mapXY.mapY!!, mapXY.mapX!!), 10.0)
        naverMap.cameraPosition = cameraPosition

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapView.onDestroy()
    }

}
