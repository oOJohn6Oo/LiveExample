package io.agora.live.livegame.di

import io.agora.live.livegame.shared.Platform
import org.koin.core.context.startKoin


val dataStore = Platform.createDataStore()

fun initKoin() {
    startKoin {
    }
}
