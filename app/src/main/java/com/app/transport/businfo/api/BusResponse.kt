package com.app.transport.businfo.api

import com.fasterxml.jackson.annotation.JsonProperty

data class BusResponse(
    val id: Int,
    val code: String,
    val name: String,
    val sort: Int?,
    val display: Int?,
    @JsonProperty("colour")
    val color: String?,
    val idBusTypes: Int?,
    val sortApk: Int?,
    val price: Int,
    val version: Int?,
)