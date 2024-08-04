package io.agora.live.livegame.shared

import io.agora.live.livegame.data.bean.LiveUser

actual class RTC actual constructor() {
    actual fun loginRoom(
        roomId: String,
        user: LiveUser,
        owner: Boolean
    ) {
    }

    actual fun logoutRoom() {
    }

    actual fun enableLocalAudio(enable: Boolean) {
    }

    actual fun enableLocalVideo(enable: Boolean) {
    }

    /**
     * 显示摄像头画面
     *
     * @param view 显示视频流的View
     * @param remoteStreamId 不填表示预览本机摄像头画面
     */
    actual fun previewCamera(view: Any, remoteStreamId: String) {
    }

    actual fun createDataStream(): Int {
        return -2
    }

    /**
     * 控制是否发布音频流
     *
     * @param mute true 不发布; false 发布
     */
    actual fun muteLocalAudioStream(mute: Boolean) {
    }

    actual fun muteLocalVideoStream(mute: Boolean) {
    }

    actual fun flipCamera(useFront: Boolean) {
    }

    actual fun publishStream(streamId: String) {
    }

    actual fun stopPublishingStream() {
    }

    actual fun attachCallback() {
    }

    actual fun removeCallback() {
    }

    actual fun sendMessage(msg: String, streamId: Int) {
    }

    actual fun stopPreview() {
    }

    actual companion object {
        actual fun init() {
        }

        actual fun destroy() {
        }

        actual var onStreamMessage: ((uid: Int, streamId: Int, data: ByteArray?) -> Unit)? = null
    }

}