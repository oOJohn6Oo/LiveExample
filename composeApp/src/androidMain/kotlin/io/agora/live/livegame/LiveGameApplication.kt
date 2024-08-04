package io.agora.live.livegame

import android.app.Application
import android.content.Context
import io.agora.live.livegame.di.initKoin

class LiveGameApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }
    
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
    
    companion object{
        lateinit var instance: LiveGameApplication
    }
}
