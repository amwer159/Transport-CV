package com.app.transport.businfo.data

import BusClient
import com.app.transport.businfo.api.BusResponse
import com.app.transport.businfo.domain.BusInfoRepository

class BusInfoRepositoryImpl(private val budClient: BusClient): BusInfoRepository {

    override suspend fun execute(): Map<String, BusResponse> {
        return budClient.fetchBusesInfo()
    }
}