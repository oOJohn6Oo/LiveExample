package io.agora.live.livegame

object AppConfig {
    const val AGORA_APP_ID = ""
    const val AGORA_APP_TOKEN = ""
}

enum class LiveChannel(val channelName:String){
    PK("pk"),
    COM("com"),
    VS("vs"),
}