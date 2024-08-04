package io.agora.live.livegame.data.bean

import io.agora.live.livegame.data.LocalData
import kotlinx.serialization.Serializable

/**
 * @param id room ID
 * @param userId the uesr ID of the room owner
 */
@Serializable
data class RoomInfo(
    val id: String,
    val name: String,
    val userId: String,
    val cover: String = LocalData.localCover[0],
    val createTime: Long
)