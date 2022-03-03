package io.agora.live.livegame

object AppConfig {
    const val AGORA_APP_ID = "sdf"
    const val AGORA_APP_TOKEN = "sdf"
}

enum class LiveChannel(val channelName:String){
    PK("pk"),
    COM("com"),
    VS("vs"),
}