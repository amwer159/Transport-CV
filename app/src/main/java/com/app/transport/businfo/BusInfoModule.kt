package com.app.transport.businfo

import BusClient
import com.app.transport.businfo.data.BusInfoRepositoryImpl
import com.app.transport.businfo.domain.BusInfoRepository
import com.app.transport.businfo.domain.LoadBusInfoUseCase
import com.app.transport.map.MapIntentTransformer
import com.app.transport.map.MapIntentTransformerImpl
import com.app.transport.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val busInfoModule = module {

    single { get<Retrofit>().create(BusClient::class.java) }

    single<BusInfoRepository> { BusInfoRepositoryImpl(get()) }

    factory { LoadBusInfoUseCase(get()) }

    factory<MapIntentTransformer> { MapIntentTransformerImpl(get()) }
    viewModel { MapViewModel(mapIntentTransformer = get()) }
}