package io.agora.live.livegame.android

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.agora.live.livegame.AppConfig
import io.agora.live.livegame.LiveChannel
import io.agora.rtc2.*
import io.agora.rtc2.RtcEngineConfig.LogConfig
import io.agora.syncmanager.rtm.Sync
import io.agora.syncmanager.rtm.SyncManagerException
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        const val RTM_SDK = 0
        const val RTC_SDK = 1
        var rtcEngineEx:RtcEngineEx? = null
    }

    val sdkState = mutableStateOf(0)

    fun initSDK(channel:LiveChannel){
        viewModelScope.launch {
//            initRTC()
//            initSync(channel)
        }
    }

    fun destroySDK(){
        viewModelScope.launch {
            RtcEngine.destroy()
            try {
                Sync.Instance().destroy()
            } catch (e: Exception) {
            }
        }
    }

    private fun initRTC() {
        val context = getApplication<Application>().applicationContext
        val appID: String = AppConfig.AGORA_APP_ID
        if (appID.isEmpty() || appID.codePointCount(0, appID.length) != 32) {
            setInitResult(RTC_SDK, false)
        } else {
            val logConfig = LogConfig().apply {
                filePath = context.externalCacheDir?.absolutePath?:context.cacheDir.absolutePath
            }
            val config = RtcEngineConfig().apply {
                mContext = context
                mAppId = appID
                mLogConfig = logConfig
                mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            }

            try {
                val engine = RtcEngineEx.create(config) as RtcEngineEx
                engine.enableAudio()
                engine.enableVideo()
                rtcEngineEx = engine
                setInitResult(RTC_SDK, true)
            } catch (e: Exception) {
                e.printStackTrace()
                setInitResult(RTC_SDK, false)
            }
        }
    }

    private fun setInitResult(sdkType: Int, b: Boolean) {

    }

    private fun initSync(channel:LiveChannel) {
        val map = HashMap<String, String>()
        map["appid"] = AppConfig.AGORA_APP_ID
        map["token"] = AppConfig.AGORA_APP_TOKEN
        map["defaultChannel"] = channel.channelName
        Sync.Instance().init(getApplication(), map, object :Sync.Callback{
            override fun onSuccess() {
                setInitResult(RTM_SDK, true)
            }

            override fun onFail(exception: SyncManagerException?) {
                setInitResult(RTM_SDK, false)
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        RtcEngine.destroy()
        Sync.Instance().destroy()
    }
}