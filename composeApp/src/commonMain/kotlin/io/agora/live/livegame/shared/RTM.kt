package io.agora.live.livegame.shared

import io.agora.live.livegame.data.BaseStateCallback
import io.agora.live.livegame.LiveChannel
import io.agora.live.livegame.data.bean.RoomInfo

expect class RTM() {
    internal val callbackMap:HashMap<String, BaseStateCallback<*>>

    fun getRoomList(callback: BaseStateCallback<List<String>>)

    fun joinRoom(roomId: String)

    fun deleteRoom(roomId: String)

    fun createRoom(roomInfo: RoomInfo, callback: BaseStateCallback<Unit>)

    fun unregisterCallback(callback: BaseStateCallback<*>)

    companion object{
        fun init(channel: LiveChannel, onSuccess: () -> Unit)
        fun destroy()
    }
}