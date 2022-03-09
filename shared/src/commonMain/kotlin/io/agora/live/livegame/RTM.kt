package io.agora.live.livegame

import io.agora.live.livegame.bean.RoomInfo

expect class RTM() {
    internal val callbackMap:HashMap<String,BaseStateCallback<*>>

    fun getRoomList(callback: BaseStateCallback<List<String>>)

    fun joinRoom(roomId: String)

    fun createRoom(roomInfo: RoomInfo, callback: BaseStateCallback<Unit>)

    fun unregisterCallback(callback: BaseStateCallback<*>)
}