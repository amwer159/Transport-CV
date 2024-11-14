package com.app.transport.map

import com.app.transport.mvi.MviBaseViewModel

class MapViewModel(
    mapIntentTransformer: MapIntentTransformer,
) : MviBaseViewModel<MapIntent, MapResult, MapState, MapEvent>(
    intentTransformer = mapIntentTransformer,
    initialIntent = MapIntent.OnCreate,
    initialState = MapState(),
) {

    override fun stateTransformer(oldState: MapState, result: MapResult): MapState {
        return when (result) {
            is MapResult.Location -> oldState.copy(location = result.location)
            is MapResult.BusInfo -> oldState.copy(busInfo = result.busInfo)
        }
    }
}