package com.app.transport.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class MviBaseViewModel<Intent : Any, Result: Any, State : Any, Event : Any>(
    private val intentTransformer: MviIntentTransformer<Intent, Result, Event>,
    private val initialIntent: Intent? = null,
    private val initialState: State,
) : MviViewModel<Intent, State, Event>, ViewModel() {

    private companion object {
        /**
         * The buffer size that will be allocated by [kotlinx.coroutines.flow.MutableSharedFlow].
         * If it falls behind by more than 64 state updates, it will start suspending.
         * Slow consumers should consider using `stateFlow.buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST)`.
         *
         * The internally allocated buffer is replay + extraBufferCapacity but always allocates 2^n space.
         * We use replay=0 so buffer = 64.
         */
        private const val SubscriberBufferSize = 64
    }

    protected val tag: String = this.javaClass.simpleName

    private val state: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }

    override val viewState: StateFlow<State> = state.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.UNLIMITED)
    final override val singleEvent: Flow<Event> get() = eventChannel.receiveAsFlow()

    private val intentMutableFlow: MutableSharedFlow<Intent> = MutableSharedFlow(extraBufferCapacity = SubscriberBufferSize)

    private var initialIntentIsEmitted: Boolean = false

    override suspend fun processIntent(intent: Intent) {
        intentMutableFlow.emit(intent)
    }

    protected val intentFlow: SharedFlow<Intent> = intentMutableFlow.asSharedFlow()

    init {
        // TODO: Pass Coroutine context via constructor
        viewModelScope.launch(Dispatchers.Default + SupervisorJob()) {
            intentMutableFlow
                .onStart {
                    if (initialIntent != null && !initialIntentIsEmitted) {
                        emit(initialIntent)
                        initialIntentIsEmitted = true
                    }
                }
                .flatMapMerge(DEFAULT_CONCURRENCY, intentTransformer::transform)
                .scan(state.value, ::stateTransformer)
                .collect(state)
        }
    }

    protected abstract fun stateTransformer(oldState: State, result: Result): State

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
    }

    // Send event and access intent flow.
    protected suspend fun sendEvent(event: Event) {
        // TODO: Provide coroutine context via ctor
        withContext(Dispatchers.Main.immediate) {
            eventChannel.send(event)
        }
    }


    protected fun <T> Flow<T>.shareWhileSubscribed(): SharedFlow<T> =
        shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    protected fun <T> Flow<T>.stateWithInitialNullWhileSubscribed(): StateFlow<T?> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
