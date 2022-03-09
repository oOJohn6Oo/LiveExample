package io.agora.live.livegame

import io.agora.live.livegame.bean.RoomInfo
import io.agora.syncmanager.rtm.*

actual class RTM actual constructor() {

    internal actual val callbackMap: HashMap<String, BaseStateCallback<*>> = hashMapOf()

    actual fun getRoomList(callback: BaseStateCallback<List<String>>) {
        callbackMap["getRoomList"] = callback
        try {
            Sync.Instance().getScenes(object : Sync.DataListCallback {
                override fun onSuccess(result: MutableList<IObject>?) {
                    val res: List<String> = result?.let { receivedData ->
                        val tempResList = mutableListOf<String>()
                        receivedData.forEach { data ->
                            tempResList.add(data.toString())
                        }
                        tempResList.toList()
                    } ?: listOf()
                    callbackMap["getRoomList"]?.let {
                        (it as BaseStateCallback<List<String>>).onSuccess(res)
                    }
                }
                override fun onFail(exception: SyncManagerException) {
                    callbackMap["getRoomList"]?.onFailure(exception)
                }
            })
        } catch (e: Exception) {
            callbackMap["getRoomList"]?.onFailure(e)
        }
    }

    actual fun joinRoom(roomId: String) {
        Sync.Instance().joinScene(roomId, object : Sync.JoinSceneCallback {
            override fun onSuccess(sceneReference: SceneReference?) {

            }

            override fun onFail(exception: SyncManagerException?) {

            }
        })
    }

    actual fun createRoom(roomInfo: RoomInfo, callback: BaseStateCallback<Unit>) {
        callbackMap["createRoom"] = callback

        val map = mapOf(
            "name" to roomInfo.name,
            "ownerId" to roomInfo.ownerId,
            "cover" to roomInfo.cover,
            "createTime" to roomInfo.createTime.toString(),
        )

        val scene = Scene().apply {
            id = roomInfo.id
            userId = roomInfo.ownerId
            property = map
        }

        Sync.Instance().createScene(scene, object : Sync.Callback {
            override fun onSuccess() {
                callbackMap["createRoom"]?.let {
                    (it as BaseStateCallback<Unit>).onSuccess(Unit)
                }
            }

            override fun onFail(exception: SyncManagerException) {
                callbackMap["createRoom"]?.onFailure(exception)
            }

        })
    }

    actual fun unregisterCallback(callback: BaseStateCallback<*>) {
        callbackMap.keys.forEach {
            if (callbackMap[it] == callback)
                callbackMap.remove(it)
        }
    }

}