package com.app.transport.mvi

import androidx.annotation.CheckResult
import kotlinx.coroutines.flow.Flow

/**
 * Object representing a UI that will
 * a) emit its intents to a view model,
 * b) subscribes to a view model for rendering its UI.
 * c) subscribes to a view model for handling single UI event.
 */
interface MviView<Intent : Any, State : Any, Event : Any> {
    /**
     * Entry point for the [MviView] to render itself based on a [Any].
     */
    fun drawState(viewState: State)

    /**
     * Entry point for the [MviView] to handle single event.
     */
    fun handleSingleEvent(event: Event) = Unit

    /**
     * Unique [Flow] used by the [MviViewModel] to listen to the [MviView].
     * All the [MviView]'s [Any]s must go through this [Flow].
     */
    @CheckResult
    fun viewIntents(): Flow<Intent>
}
