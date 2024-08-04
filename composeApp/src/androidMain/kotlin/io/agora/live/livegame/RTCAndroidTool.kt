package io.agora.live.livegame

import android.app.Application
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.shared.RTC
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.internal.RtcEngineImpl

class RTCAndroidTool {

    companion object {

        var agoraRTC: RtcEngineImpl? = null

        fun initAgoraRTC(app: Application): Boolean {
            if(agoraRTC != null) return true
            val appID: String = AppConfig.AGORA_APP_ID
            if (appID.isEmpty() || appID.codePointCount(0, appID.length) != 32) {
                return false
            } else {
                val logConfig = RtcEngineConfig.LogConfig().apply {
                    level = Constants.LogLevel.getValue(Constants.LogLevel.LOG_LEVEL_NONE)
                    filePath =
                        app.externalCacheDir?.absolutePath ?: app.cacheDir.absolutePath
                }
                val config = RtcEngineConfig().apply {
                    mContext = app
                    mAppId = appID
                    mEventHandler = object : IRtcEngineEventHandler() {
                        override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray?) {
                            super.onStreamMessage(uid, streamId, data)
                            Platform.logD("lq", "onStreamMessage:uid:$uid, streamId:$streamId, data:$data")
                            RTC.onStreamMessage?.invoke(uid, streamId, data)
                        }
                    }
                    mLogConfig = logConfig
                    mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
                }

                return try {
                    agoraRTC = (RtcEngine.create(config) as? RtcEngineImpl)?.apply {
                        setLogFilter(Constants.LOG_FILTER_OFF)
                    }
                    Platform.logD("lq", "agoraRTC:$agoraRTC")
                    true
                } catch (e: Exception) {
                    Platform.logD("lq", e.message ?: "")
                    e.printStackTrace()
                    false
                }
            }
        }

        fun destroy() {
            Platform.logD("lq", "RTC destroy")
//            ZegoExpressEngine.destroyEngine(null)
        }
    }
}