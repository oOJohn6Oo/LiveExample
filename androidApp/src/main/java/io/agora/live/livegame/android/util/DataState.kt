package io.agora.live.livegame.android.util

sealed class DataState<out R> {
    data class Success<out T>(val data:T): DataState<T>()
    data class Failure(val exception: Throwable): DataState<Nothing>()
    object Loading: DataState<Nothing>()
    object None:DataState<Nothing>()
}