package com.trip.tripsync.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.trip.tripsync.ui.activity.MainActivity.Companion.PERMISSION_REQUEST_CODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class LocationUtility(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    fun requestLocationUpdate(onSuccessListener: OnSuccessListener<Location?>) {
        try {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.lastLocation.addOnSuccessListener(onSuccessListener)
                } else {
                    val activity = context as? Activity
                    activity?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                }

    } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    fun stopLocationUpdate() {
        locationCallback?.let {
            try {
                fusedLocationClient.removeLocationUpdates(it)
                locationCallback = null
            } catch (e:SecurityException) {
                e.printStackTrace()
            }
        }
    }

}