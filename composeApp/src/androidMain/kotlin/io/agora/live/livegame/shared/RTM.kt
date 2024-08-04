package io.agora.live.livegame.shared

import io.agora.live.livegame.SyncAndroidTool
import io.agora.live.livegame.data.BaseStateCallback
import io.agora.live.livegame.LiveChannel
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.syncmanager.rtm.IObject
import io.agora.syncmanager.rtm.Scene
import io.agora.syncmanager.rtm.SceneReference
import io.agora.syncmanager.rtm.Sync
import io.agora.syncmanager.rtm.SyncManagerException

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

    actual fun deleteRoom(roomId: String){
        Sync.Instance().deleteScene(roomId, object : Sync.Callback {
            override fun onSuccess() {
            }

            override fun onFail(exception: SyncManagerException?) {

            }
        })
    }

    actual fun createRoom(roomInfo: RoomInfo, callback: BaseStateCallback<Unit>) {
        callbackMap["createRoom"] = callback

        val map = mapOf(
            "name" to roomInfo.name,
            "cover" to roomInfo.cover,
            "createTime" to roomInfo.createTime,
        )

        val scene = Scene().apply {
            id = roomInfo.id
            userId = roomInfo.userId
            property = map
        }

        Sync.Instance().createScene(scene, object : Sync.Callback {
            override fun onSuccess() {
                Platform.logD("lq", "createScene onSuccess")

                callbackMap["createRoom"]?.let {
                    (it as BaseStateCallback<Unit>).onSuccess(Unit)
                }
            }

            override fun onFail(exception: SyncManagerException) {
                Platform.logD("lq", "createScene onFail")
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

    actual companion object{
        actual fun init(channel: LiveChannel, onSuccess: ()->Unit) {
            SyncAndroidTool.initSync(channel, onSuccess)
        }

        actual fun destroy(){
            SyncAndroidTool.destroy()
        }
    }

}