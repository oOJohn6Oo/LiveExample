package io.agora.live.livegame.data.bean

import kotlinx.serialization.Serializable

@Serializable
data class LiveScene(val index:Int, val name: String, val desc: String)
