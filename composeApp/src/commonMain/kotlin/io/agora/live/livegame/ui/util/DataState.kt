package io.agora.live.livegame.ui.util

import kotlinx.serialization.json.Json

sealed class DataState<out R> {
    data class Success<out T>(val data:T): DataState<T>()
    data class Failure(val exception: Throwable): DataState<Nothing>()
    data object Loading: DataState<Nothing>()
    data object None: DataState<Nothing>()
}


val LGJson = Json{
    ignoreUnknownKeys = true
}