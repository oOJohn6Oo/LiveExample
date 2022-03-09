package io.agora.live.livegame

object AppConfig {
    const val AGORA_APP_ID = ""
    const val AGORA_APP_TOKEN = ""

    const val ZEGO_APP_ID = 0L
    const val ZEGO_APP_SIGN = ""
}

enum class LiveChannel(val channelName:String){
    PK("pk"),
    COM("com"),
    VS("vs"),
}