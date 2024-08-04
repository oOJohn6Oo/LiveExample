package io.agora.live.livegame.data

interface BaseStateCallback<T> {
    fun onSuccess(data: T)

    fun onFailure(exception: Throwable)
}