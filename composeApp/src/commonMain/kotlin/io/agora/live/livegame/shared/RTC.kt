package io.agora.live.livegame.shared

import io.agora.live.livegame.data.bean.LiveUser

expect class RTC() {
    fun loginRoom(roomId: String, user: LiveUser, owner: Boolean)

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

    fun createDataStream():Int
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

    fun attachCallback()

    fun removeCallback()

    fun sendMessage(msg:String, streamId: Int)

    fun stopPreview()

    companion object {
        fun init()
        fun destroy()
        var onStreamMessage: ((uid: Int, streamId: Int, data: ByteArray?) -> Unit)?
    }
}