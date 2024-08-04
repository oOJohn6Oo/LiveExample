package io.agora.live.livegame.data.bean

import io.agora.live.livegame.data.LocalData
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic
import kotlin.random.Random

@Serializable
data class LiveUser(val id: String, val name: String, val avatar: String) {
    companion object {
        @JvmStatic
        fun getDefaultInstance() = with(Random.nextInt(Int.MAX_VALUE)) {
            LiveUser(
                this.toString(),
                "User-$this",
                LocalData.localAvatar[this % LocalData.localAvatar.size]
            )
        }

    }
}