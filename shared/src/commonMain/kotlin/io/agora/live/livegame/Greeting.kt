package io.agora.live.livegame

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}