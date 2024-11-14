package com.app.transport.mvi

import kotlinx.coroutines.flow.Flow

interface MviIntentTransformer<in Intent: Any, out Result: Any, Event: Any> {
    fun transform(intent: Intent): Flow<Result>
}