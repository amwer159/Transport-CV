package com.app.transport.app

import android.app.Application
import com.app.transport.common.TransportKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TransportApp : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@TransportApp)
            modules(TransportKoinModules.modules)
        }
    }
}