package io.agora.live.livegame.shared

import io.agora.live.livegame.LiveChannel
import io.agora.live.livegame.data.BaseStateCallback
import io.agora.live.livegame.data.bean.RoomInfo

actual class RTM actual constructor(){
    internal actual val callbackMap: HashMap<String, BaseStateCallback<*>> = hashMapOf()

    actual fun getRoomList(callback: BaseStateCallback<List<String>>) {
    }

    actual fun joinRoom(roomId: String) {
    }

    actual fun deleteRoom(roomId: String) {
    }

    actual fun createRoom(
        roomInfo: RoomInfo,
        callback: BaseStateCallback<Unit>
    ) {
    }

    actual fun unregisterCallback(callback: BaseStateCallback<*>) {
        callbackMap.keys.forEach {
            if (callbackMap[it] == callback)
                callbackMap.remove(it)
        }
    }

    actual companion object{
        actual fun init(channel: LiveChannel, onSuccess: () -> Unit) {
        }
        actual fun destroy() {
        }

    }

}