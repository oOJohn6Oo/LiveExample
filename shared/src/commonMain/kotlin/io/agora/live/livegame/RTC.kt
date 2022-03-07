package io.agora.live.livegame

import io.agora.live.livegame.bean.LiveUser

expect class RTC() {
    fun joinChannel(channelName:String, user: LiveUser, amHost:Boolean = false)

    fun leaveAllChannel()

    fun enableLocalAudio(enable:Boolean = true)

    fun enableLocalVideo(enable:Boolean = true)

    fun previewLocalCamera(view:Any)

    fun muteLocalAudioStream(mute:Boolean = true)

    fun muteLocalVideoStream(mute:Boolean = true)

    fun flipCamera()

    fun startPreview()

    fun stopPreview()
}