package com.app.transport.mvi

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class MviBaseActivity
<Intent : Any, State : Any, Event : Any, ViewModel : MviViewModel<Intent, State, Event>>(
    @LayoutRes contentLayoutId: Int,
) : AppCompatActivity(contentLayoutId), MviView<Intent, State, Event> {
    protected abstract val viewModel: ViewModel

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        subscribeUI()
    }

    protected abstract fun initUI()

    private fun subscribeUI() {
        viewModel.viewState
            .onEach { drawState(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewModel.singleEvent
            .onEach { handleSingleEvent(it) }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)

        viewIntents()
            .onEach { viewModel.processIntent(it) }
            .launchIn(lifecycleScope)
    }
}
