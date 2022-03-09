package io.agora.live.livegame.bean

import io.agora.live.livegame.LocalData

data class RoomInfo(
    val id: String,
    val name: String,
    val ownerId: String,
    val cover: String = LocalData.localCover[0],
    val createTime: Long
)