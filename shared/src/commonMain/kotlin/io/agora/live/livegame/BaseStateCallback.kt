package io.agora.live.livegame

interface BaseStateCallback<T> {
    fun onSuccess(data: T)

    fun onFailure(exception: Throwable)
}