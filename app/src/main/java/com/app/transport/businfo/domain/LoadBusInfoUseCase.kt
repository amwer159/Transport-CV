package com.app.transport.businfo.domain

import com.app.transport.businfo.api.BusResponse
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching

sealed class TransportException {
    data class LoadBusInfoException(val message: String, val error: Throwable) : TransportException()
}

class LoadBusInfoUseCase(private val busInfoRepository: BusInfoRepository) {

    suspend fun execute(): Result<Map<String, BusResponse>, TransportException> =
        runCatching { busInfoRepository.execute() }
            .mapError {
                TransportException.LoadBusInfoException("Couldn't load bus info", it)
            }
}
