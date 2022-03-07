package io.agora.live.livegame

import android.content.Context
import io.agora.rtc2.*
import io.agora.rtc2.internal.RtcEngineImpl

class RTCAndroidTool {
    companion object {
        var instance: RtcEngineImpl? = null

        fun initRTC(context: Context): Boolean {
            if (instance != null) return true

            val appID: String = AppConfig.AGORA_APP_ID
            if (appID.isEmpty() || appID.codePointCount(0, appID.length) != 32) {
                return false
            } else {
                val logConfig = RtcEngineConfig.LogConfig().apply {
                    level = Constants.LogLevel.getValue(Constants.LogLevel.LOG_LEVEL_NONE)
                    filePath =
                        context.externalCacheDir?.absolutePath ?: context.cacheDir.absolutePath
                }
                val config = RtcEngineConfig().apply {
                    mContext = context
                    mAppId = appID
                    mEventHandler = object : IRtcEngineEventHandler() {}
                    mLogConfig = logConfig
                    mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
                }

                return try {
                    val engine = RtcEngineEx.create(config) as RtcEngineImpl
                    engine.setLogFilter(Constants.LOG_FILTER_OFF)
                    engine.enableAudio()
                    engine.enableVideo()
                    instance = engine
                    true
                } catch (e: Exception) {
                    e.message?.log()
                    e.printStackTrace()
                    false
                }
            }
        }

        fun destroy() {
            "RTC destroy".log()
            RtcEngineEx.destroy()
        }
    }
}