package com.gumrindelwald.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Polyline
import com.gumrindelwald.domain.RunLocationTimestamp
import com.gumrindelwald.presentation.util.PolylineColorCalculator
import com.gumrindelwald.presentation.util.PolylineUI

@Composable
@GoogleMapComposable
fun GumRunPolylines(location: List<List<RunLocationTimestamp>>) {
    // [[loc1, loc2], [loc3, loc4]]
    // transform this to:
    // [[loc1, loc2], [loc2, loc3], [loc3, loc4]]

    val polylines = remember(location) {
        location.map { loc ->
            loc.zipWithNext { loc1, loc2 ->
                PolylineUI(
                    loc1 = loc1,
                    loc2 = loc2,
                    color = PolylineColorCalculator.locationToColor(loc1, loc2)
                )
            }
        }.flatten()
    }


    polylines.forEach { polyline ->
        Polyline(
            points = listOf(
                LatLng(polyline.loc1.location.lat, polyline.loc1.location.long),
                LatLng(polyline.loc2.location.lat, polyline.loc2.location.long)
            ),
            color = polyline.color,
            jointType = JointType.BEVEL
        )
    }
}
