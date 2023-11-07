package com.trip.tripsync.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    var mapXY = MutableLiveData<Pair<Double, Double>> (0.0 to 0.0)
}