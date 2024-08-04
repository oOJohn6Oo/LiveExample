package io.agora.live.livegame.shared

import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import io.agora.live.livegame.LiveGameApplication
import io.agora.live.livegame.RTCAndroidTool
import io.agora.live.livegame.SyncAndroidTool
import io.agora.live.livegame.data.bean.LiveUser
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.DataStreamConfig
import io.agora.rtc2.video.VideoCanvas

actual class RTC {

    actual fun loginRoom(roomId: String, user: LiveUser, owner: Boolean) {
        RTCAndroidTool.agoraRTC?.joinChannel(
            "",
            roomId,
            user.id.toInt(),
            ChannelMediaOptions().apply {
                autoSubscribeAudio = true
                autoSubscribeVideo = true
                if (owner) {
                    publishCameraTrack = true
                    publishMicrophoneTrack = true
// 设置用户角色为 BROADCASTER (主播) 或 AUDIENCE (观众)
                    clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
// 设置频道场景为 BROADCASTING (直播场景)
                    channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
// 发布麦克风采集的音频
                    publishMicrophoneTrack = true;
// 发布摄像头采集的视频
                    publishCameraTrack = true;
                } else {
// 设置用户角色为 BROADCASTER (主播) 或 AUDIENCE (观众)
                    clientRoleType = Constants.CLIENT_ROLE_AUDIENCE
                }
            })
    }

    actual fun createDataStream():Int {
        return RTCAndroidTool.agoraRTC?.createDataStream(DataStreamConfig()) ?: -2
    }

    actual fun logoutRoom() {
        RTCAndroidTool.agoraRTC?.leaveChannel()
    }

    actual fun enableLocalAudio(enable: Boolean) {
        RTCAndroidTool.agoraRTC?.enableLocalAudio(enable)
    }

    actual fun enableLocalVideo(enable: Boolean) {
        RTCAndroidTool.agoraRTC?.enableLocalVideo(enable)
    }

    actual fun muteLocalAudioStream(mute: Boolean) {
        RTCAndroidTool.agoraRTC?.muteLocalAudioStream(mute)
    }

    actual fun muteLocalVideoStream(mute: Boolean) {
        RTCAndroidTool.agoraRTC?.muteLocalVideoStream(mute)
    }

    actual fun flipCamera(useFront: Boolean) {
        RTCAndroidTool.agoraRTC?.switchCamera()
    }

    actual fun previewCamera(view: Any, remoteStreamId: String) {
        if (view is SurfaceView || view is TextureView) {
            if (remoteStreamId.isBlank()) {
                // 空id加载本机camera
                RTCAndroidTool.agoraRTC?.setupLocalVideo(VideoCanvas(view as View))
                RTCAndroidTool.agoraRTC?.startPreview()
            } else {
                // 加载远程视频流
                RTCAndroidTool.agoraRTC?.setupRemoteVideo(VideoCanvas(view as View, VideoCanvas.RENDER_MODE_FIT, remoteStreamId.toInt()))
                RTCAndroidTool.agoraRTC?.startPreview()
            }
        } else {
            Platform.logD("lq", "previewCamera do not support ${view::class.java}")
        }
    }

    actual fun stopPreview() {
        RTCAndroidTool.agoraRTC?.stopPreview()
    }

    actual fun publishStream(streamId: String) {
        RTCAndroidTool.agoraRTC?.updateChannelMediaOptions(ChannelMediaOptions().apply {
            autoSubscribeAudio = true
            autoSubscribeVideo = true
            publishCameraTrack = true
            publishMicrophoneTrack = true
        })
    }

    actual fun stopPublishingStream() {
        RTCAndroidTool.agoraRTC?.updateChannelMediaOptions(ChannelMediaOptions().apply {
            autoSubscribeAudio = true
            autoSubscribeVideo = true
            publishCameraTrack = false
            publishMicrophoneTrack = false
        })
    }

    actual fun attachCallback() {
//        RTCAndroidTool.agoraRTC?.
//        ZegoExpressEngine.getEngine()?.setEventHandler(object : IZegoEventHandler(){
//            override fun onRoomStreamUpdate(
//                roomID: String?,
//                updateType: ZegoUpdateType?,
//                streamList: ArrayList<ZegoStream>?,
//                extendedData: JSONObject?
//            ) {
//                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
//                Platform.logD("lq", "-------START------")
//                streamList?.forEach {
//                    Platform.logD("lq", "${it.extraInfo}\n${it.streamID}\n${it.user.userID}")
//                }
//            }
//        })
    }

    actual fun sendMessage(msg:String, streamId: Int){
        val res = RTCAndroidTool.agoraRTC?.sendStreamMessage(streamId, msg.toByteArray())
        Platform.logD("lq", "sendMessage:$res")
    }

    actual fun removeCallback() {
//        RTCAndroidTool.agoraRTC?.hand
//        ZegoExpressEngine.getEngine()?.setEventHandler(null)
    }

    actual companion object {
        actual fun init() {
            RTCAndroidTool.initAgoraRTC(LiveGameApplication.instance)
        }

        actual fun destroy() {
            SyncAndroidTool.destroy()
        }
        actual var onStreamMessage: ((uid: Int, streamId: Int, data: ByteArray?) -> Unit)? = null
    }
}