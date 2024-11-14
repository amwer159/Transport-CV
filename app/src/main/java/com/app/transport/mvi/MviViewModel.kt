package com.app.transport.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<Intent : Any, State : Any, Event : Any> {
    val viewState: StateFlow<State>

    val singleEvent: Flow<Event>

    suspend fun processIntent(intent: Intent)
}
