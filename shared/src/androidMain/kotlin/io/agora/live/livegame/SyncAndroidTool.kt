package io.agora.live.livegame

import android.content.Context
import io.agora.syncmanager.rtm.Sync
import io.agora.syncmanager.rtm.SyncManagerException
import java.util.concurrent.atomic.AtomicBoolean

class SyncAndroidTool {
    companion object{

        private var isSyncInit:AtomicBoolean = AtomicBoolean(false)

        /**
         * 初始化 RTM
         *
         * TODO 如何监听 RTM 初始化成功
         */
        fun initSync(context: Context, channel: LiveChannel){
            "initSync".log()
            val map = HashMap<String, String>()
            map["appid"] = AppConfig.AGORA_APP_ID
            map["token"] = AppConfig.AGORA_APP_TOKEN
            map["defaultChannel"] = channel.channelName
            Sync.Instance().init(context, map, object :Sync.Callback{
                override fun onSuccess() {
                    "Sync init success".log()
                    isSyncInit.compareAndSet(false, true)
                }

                override fun onFail(exception: SyncManagerException) {
                    "Sync init onFail:${exception.message}".log()
                    isSyncInit.set(false)
                }
            })
        }

        fun destroy(){
            if (isSyncInit.get()) Sync.Instance().destroy()
        }
    }

}