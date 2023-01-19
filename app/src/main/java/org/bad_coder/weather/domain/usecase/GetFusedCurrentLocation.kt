package org.bad_coder.weather.domain.usecase

import android.annotation.SuppressLint
import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.bad_coder.weather.data.Resource
import org.bad_coder.weather.domain.model.CurrentLocation
import javax.inject.Inject

class GetFusedCurrentLocation @Inject constructor(private val application: Application) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    @SuppressLint("MissingPermission")
    operator fun invoke() = callbackFlow<Resource<CurrentLocation>> {
        trySend(Resource.Loading("Getting current location"))
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location ->
            trySend(
                location?.let {
                    Resource.Success(
                        CurrentLocation(
                            location.latitude,
                            location.longitude
                        )
                    )
                } ?: Resource.Error("Error getting location")
            )
        }

        awaitClose { channel.close() }
    }
}