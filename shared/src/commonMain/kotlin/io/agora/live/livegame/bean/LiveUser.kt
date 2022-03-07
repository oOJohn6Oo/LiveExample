package io.agora.live.livegame.bean

import io.agora.live.livegame.LocalData
import kotlin.jvm.JvmStatic
import kotlin.random.Random

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