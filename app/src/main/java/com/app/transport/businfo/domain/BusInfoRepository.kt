package com.app.transport.businfo.domain

import com.app.transport.businfo.api.BusResponse

interface BusInfoRepository {
    suspend fun execute(): Map<String, BusResponse>
}