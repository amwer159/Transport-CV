package com.app.transport.map

import com.app.transport.businfo.api.BusResponse
import com.google.android.gms.maps.model.LatLng

sealed class MapIntent {
    object OnCreate : MapIntent()
    object MoveToLocation : MapIntent()
}

sealed class MapResult {
    data class Location(val location: LatLng) : MapResult()
    data class BusInfo(val busInfo: Map<String, BusResponse>) : MapResult()
}

data class MapState(
    val location: LatLng? = null,
    val busInfo: Map<String, BusResponse> = emptyMap(), // TODO: Replace with UI/domain module
)

sealed class MapEvent
