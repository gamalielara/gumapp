package com.gumrindelwald.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.gumrindelwald.domain.LocationObserver
import com.gumrindelwald.domain.RunLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationObserver(private val context: Context) : LocationObserver {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    override fun observeLocation(interval: Long): Flow<RunLocation> {
        // Make sure if we have permission otherwise it might break/crash
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGPSEnabled = false
            var isNetworkEnabled = false

            // Try and retry to enable GPS and Network
            while (!isGPSEnabled && !isNetworkEnabled) {
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGPSEnabled && !isNetworkEnabled) {
                    delay(3000L)
                }
            }

            val isFineLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val isCoarseLocationGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            
            if (!isFineLocationGranted && !isCoarseLocationGranted) {
                close()
            } else {
                // Fire ONCE when first observing
                client.lastLocation.addOnSuccessListener {
                    it.let { loc ->
                        trySend(RunLocation(lat = loc.latitude, long = loc.longitude))
                    }
                }

                val request =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()

                // Continuous updates
                val locationCalback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.locations.forEach {
                            trySend(RunLocation(lat = it.latitude, long = it.longitude))
                        }
                    }
                }

                client.requestLocationUpdates(request, locationCalback, null)

                awaitClose {
                    client.removeLocationUpdates(locationCalback)
                }

            }
        }
    }
}