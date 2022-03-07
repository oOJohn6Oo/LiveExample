package io.agora.live.livegame

import android.view.SurfaceView
import io.agora.live.livegame.bean.LiveUser
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas

actual class RTC {

    actual fun joinChannel(channelName: String, user: LiveUser, amHost:Boolean){
        RTCAndroidTool.instance?.also {

            val options = ChannelMediaOptions().apply {
                autoSubscribeAudio = true
                autoSubscribeVideo = true
                publishAudioTrack = amHost
                publishCameraTrack = amHost
                clientRoleType =
                    if (amHost) Constants.CLIENT_ROLE_BROADCASTER else Constants.CLIENT_ROLE_AUDIENCE
            }
            // Token, channelName, uId,
            it.joinChannel(AppConfig.AGORA_APP_TOKEN, channelName, user.id.toInt(), options)
        }
    }


    actual fun leaveAllChannel() {
        RTCAndroidTool.instance?.leaveChannel()
    }

    actual fun enableLocalAudio(enable: Boolean) {
        RTCAndroidTool.instance?.enableLocalAudio(enable)
    }

    actual fun enableLocalVideo(enable: Boolean) {
        RTCAndroidTool.instance?.enableLocalVideo(enable)
    }

    actual fun muteLocalAudioStream(mute: Boolean) {
        RTCAndroidTool.instance?.muteLocalAudioStream(mute)
    }

    actual fun muteLocalVideoStream(mute: Boolean) {
        RTCAndroidTool.instance?.muteLocalVideoStream(mute)
    }

    actual fun flipCamera() {
        RTCAndroidTool.instance?.switchCamera()
    }

    actual fun startPreview() {
        val sdkVersion = RtcEngine.getSdkVersion()

        val res = RTCAndroidTool.instance?.startPreview()?:0
        "startPreview($sdkVersion):$res -${RtcEngine.getErrorDescription(res)}".log()
    }

    actual fun previewLocalCamera(view: Any){
        if (view is SurfaceView){
            val engine = RTCAndroidTool.instance
            val res = engine?.setupLocalVideo(VideoCanvas(view, Constants.RENDER_MODE_HIDDEN, 0))
            "previewLocalCamera:$res".log()
        }
//            RTCAndroidTool.instance?.setupLocalVideo(VideoCanvas(view, Constants.RENDER_MODE_FIT, 0))
    }

    actual fun stopPreview() {
        RTCAndroidTool.instance?.stopPreview()
    }
}