package io.agora.live.livegame

import android.view.SurfaceView
import android.view.TextureView
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.constants.ZegoViewMode
import im.zego.zegoexpress.entity.ZegoCanvas
import im.zego.zegoexpress.entity.ZegoRoomConfig
import im.zego.zegoexpress.entity.ZegoStream
import im.zego.zegoexpress.entity.ZegoUser
import io.agora.live.livegame.bean.LiveUser
import org.json.JSONObject
import java.util.ArrayList

actual class RTC {

    actual fun loginRoom(roomId: String, user: LiveUser) {
        ZegoExpressEngine.getEngine()
            ?.loginRoom(roomId, ZegoUser(user.id, user.name), ZegoRoomConfig().apply {
                this.isUserStatusNotify = true
                this.token = AppConfig.AGORA_APP_TOKEN
            })
    }

//    actual fun joinChannel(roomId: String, user: LiveUser, amHost:Boolean){
//        ZegoExpressEngine.getEngine()?.also {
//
//            it.loginRoom(roomId, ZegoUser(user.id, user.name), )
//            val options = ChannelMediaOptions().apply {
//                autoSubscribeAudio = true
//                autoSubscribeVideo = true
//                publishAudioTrack = amHost
//                publishCameraTrack = amHost
//                clientRoleType =
//                    if (amHost) Constants.CLIENT_ROLE_BROADCASTER else Constants.CLIENT_ROLE_AUDIENCE
//            }
//            // Token, channelName, uId,
//            it.joinChannel(AppConfig.AGORA_APP_TOKEN, channelName, user.id.toInt(), options)
//        }
//    }

    actual fun logoutRoom() {
        ZegoExpressEngine.getEngine()?.logoutRoom()
    }

    actual fun enableLocalAudio(enable: Boolean) {
        ZegoExpressEngine.getEngine()?.enableAudioCaptureDevice(enable)
    }

    actual fun enableLocalVideo(enable: Boolean) {
        ZegoExpressEngine.getEngine()?.enableCamera(enable)
    }

    actual fun muteLocalAudioStream(mute: Boolean) {
        ZegoExpressEngine.getEngine()?.mutePublishStreamAudio(mute)
    }

    actual fun muteLocalVideoStream(mute: Boolean) {
        ZegoExpressEngine.getEngine()?.mutePublishStreamVideo(mute)
    }

    actual fun flipCamera(useFront: Boolean) {
        ZegoExpressEngine.getEngine()?.useFrontCamera(useFront)
    }

    actual fun previewCamera(view: Any, remoteStreamId: String) {
        if (view is SurfaceView || view is TextureView) {
            val zegoCanvas = ZegoCanvas(view).apply {
                this.viewMode = ZegoViewMode.ASPECT_FILL
            }
            if (remoteStreamId.isBlank()) {
            // 空id加载本机camera
                ZegoExpressEngine.getEngine()?.startPreview(zegoCanvas)
            } else {
                // 加载远程视频流
                ZegoExpressEngine.getEngine()?.startPlayingStream(remoteStreamId, zegoCanvas)
            }
        } else {
            "previewCamera do not support ${view::class.java}".log()
        }
    }

    actual fun stopPreview() {
        ZegoExpressEngine.getEngine()?.stopPreview()
    }

    actual fun publishStream(streamId: String) {
        ZegoExpressEngine.getEngine()?.startPublishingStream(streamId)
    }

    actual fun stopPublishingStream() {
        ZegoExpressEngine.getEngine()?.stopPublishingStream()
    }

    fun attachCallback(){
        ZegoExpressEngine.getEngine()?.setEventHandler(object :IZegoEventHandler(){
            override fun onRoomStreamUpdate(
                roomID: String?,
                updateType: ZegoUpdateType?,
                streamList: ArrayList<ZegoStream>?,
                extendedData: JSONObject?
            ) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
                "-------START------".log()
                streamList?.forEach {
                    "${it.extraInfo}\n${it.streamID}\n${it.user.userID}".log()
                }
            }
        })
    }

    fun removeCallback(){
        ZegoExpressEngine.getEngine()?.setEventHandler(null)
    }
}