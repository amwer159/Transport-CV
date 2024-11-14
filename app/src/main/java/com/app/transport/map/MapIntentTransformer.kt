package com.app.transport.map

import com.app.transport.businfo.api.BusResponse
import com.app.transport.businfo.domain.LoadBusInfoUseCase
import com.app.transport.businfo.domain.TransportException
import com.app.transport.mvi.MviIntentTransformer
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

interface MapIntentTransformer : MviIntentTransformer<MapIntent, MapResult, MapEvent>

class MapIntentTransformerImpl(
    private val loadBusInfoUseCase: LoadBusInfoUseCase,
) : MapIntentTransformer {

    override fun transform(intent: MapIntent): Flow<MapResult> {
        return when (intent) {
            MapIntent.OnCreate -> {
                val busInfo = flow {
                    val busInfo: Result<Map<String, BusResponse>, TransportException> = loadBusInfoUseCase.execute()
                    emit(MapResult.BusInfo(emptyMap()))
                }

                busInfo
            }
            MapIntent.MoveToLocation -> {
//                val location: Flow<MapResult.Location> = flow { emit(MapResult.Location(myLocation)) }
//                location
                emptyFlow()
            }
        }
    }
}