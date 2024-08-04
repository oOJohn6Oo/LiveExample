package io.agora.live.livegame

import io.agora.live.livegame.shared.Platform
import io.agora.syncmanager.rtm.RethinkConfig
import io.agora.syncmanager.rtm.Sync
import io.agora.syncmanager.rtm.SyncManagerException
import java.util.concurrent.atomic.AtomicBoolean

class SyncAndroidTool {
    companion object{

        private var isSyncInit: AtomicBoolean = AtomicBoolean(false)

        /**
         * 初始化 RTM
         *
         * TODO 如何监听 RTM 初始化成功
         */
        fun initSync(channel: LiveChannel, onSuccess: () -> Unit) {
            Platform.logD("lq", "initSync")
//            val map = HashMap<String, String>()
//            map["appid"] = AppConfig.AGORA_APP_ID
//            map["token"] = AppConfig.AGORA_APP_TOKEN
//            map["defaultChannel"] = channel.channelName
            Sync.Instance().init(RethinkConfig(AppConfig.AGORA_APP_ID, channel.channelName),
                object : Sync.Callback {
                    override fun onSuccess() {
                        Platform.logD("lq", "Sync init success")
                        isSyncInit.compareAndSet(false, true)
                        onSuccess()
                    }

                    override fun onFail(exception: SyncManagerException) {
                        Platform.logD("lq", "Sync init onFail:${exception.message}")
                        isSyncInit.set(false)
                    }
                })
        }

        fun destroy(){
            if (isSyncInit.get()) Sync.Instance().destroy()
        }
    }

}