package io.agora.live.livegame

import io.agora.live.livegame.bean.LiveUser

expect class RTC() {
    fun loginRoom(roomId: String, user: LiveUser)

    fun logoutRoom()

    fun enableLocalAudio(enable: Boolean = true)

    fun enableLocalVideo(enable: Boolean = true)

    /**
     * 显示摄像头画面
     *
     * @param view 显示视频流的View
     * @param remoteStreamId 不填表示预览本机摄像头画面
     */
    fun previewCamera(view: Any, remoteStreamId: String = "")

    /**
     * 控制是否发布音频流
     *
     * @param mute true 不发布; false 发布
     */
    fun muteLocalAudioStream(mute: Boolean = true)

    fun muteLocalVideoStream(mute: Boolean = true)

    fun flipCamera(useFront: Boolean)

    fun publishStream(streamId: String)

    fun stopPublishingStream()

    fun stopPreview()
}