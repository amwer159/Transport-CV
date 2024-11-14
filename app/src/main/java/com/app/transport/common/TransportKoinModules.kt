package com.app.transport.common

import com.app.transport.businfo.busInfoModule

object TransportKoinModules {

    val modules = listOf(
        networkModule,
        busInfoModule,
    )
}