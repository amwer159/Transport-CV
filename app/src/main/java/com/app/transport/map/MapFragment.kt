package com.app.transport.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.transport.R
import com.app.transport.databinding.FragmentMapBinding
import com.app.transport.mvi.MviBaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : MviBaseFragment<MapIntent, MapState, MapEvent, MapViewModel>(), OnMapReadyCallback {

    override val viewModel: MapViewModel by viewModel()

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val intentsChannel = Channel<MapIntent>()

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI(savedInstanceState: Bundle?) {
        setupMap()
    }

    override fun drawState(viewState: MapState) {
        // region Map
        if (::googleMap.isInitialized) {

        }
        // endregion

    }

    override fun viewIntents(): Flow<MapIntent> =
        intentsChannel.receiveAsFlow()

    private fun setupMap() {
        (childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment).getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val myLocation = LatLng(48.289982, 25.926980)
        val chernivtsi = LatLngBounds.builder().include(myLocation).build()
        map.setLatLngBoundsForCameraTarget(chernivtsi)
        map.setMinZoomPreference(13.5f)
        map.setMaxZoomPreference(13.5f)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}