package com.app.transport.mvi

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class MviBaseFragment
<Intent : Any, State : Any, Event : Any, ViewModel : MviViewModel<Intent, State, Event>> : Fragment(), MviView<Intent, State, Event> {
    protected abstract val viewModel: ViewModel

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(savedInstanceState)
        subscribeUI()
    }

    protected abstract fun initUI(savedInstanceState: Bundle?)

    private fun subscribeUI() {
        viewModel.viewState
            .onEach { drawState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.singleEvent
            .onEach { handleSingleEvent(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewIntents()
            .onEach { viewModel.processIntent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}