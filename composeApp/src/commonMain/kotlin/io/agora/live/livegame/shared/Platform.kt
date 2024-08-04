package io.agora.live.livegame.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.agora.live.livegame.ui.LGPermissionState
import org.jetbrains.compose.resources.StringResource

expect object Platform {
    val platform: String

    val currentTimeMillis: Long

    val currentThreadName: String

    fun toast(message: String)

    fun createDataStore(): DataStore<Preferences>

    fun logD(tag: String, message: String)

    @Composable
    fun PlatformVideoView(modifier: Modifier, owner: Boolean, streamId: String, rtc: RTC)

    @Composable
    fun PreviewVideoView(modifier: Modifier, rtc: RTC)

    @Composable
    fun getppp(): Any

    fun requestPermission(state: Any)

    fun getPermissionState(state: Any): LGPermissionState
}