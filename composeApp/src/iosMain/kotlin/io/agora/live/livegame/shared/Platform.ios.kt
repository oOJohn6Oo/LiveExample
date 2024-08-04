@file:OptIn(ExperimentalForeignApi::class)

package io.agora.live.livegame.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dataStoreFileName
import doCreateDataStore
import io.agora.live.livegame.ui.LGPermissionState
import io.ktor.util.date.getTimeMillis
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSThread
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSLog

actual object Platform {
    actual val platform: String = "iOS"
    actual val currentTimeMillis: Long = getTimeMillis()
    actual val currentThreadName: String = NSThread.currentThread.name ?: ""

    actual fun toast(message: String) {
    }

    actual fun createDataStore(): DataStore<Preferences> {
        return doCreateDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            }
        )
    }

    actual fun logD(tag: String, message: String) {
        NSLog("$tag $message")
    }

    @Composable
    actual fun PlatformVideoView(
        modifier: Modifier,
        owner: Boolean,
        streamId: String,
        rtc: RTC
    ) {
    }

    @Composable
    actual fun PreviewVideoView(
        modifier: Modifier,
        rtc: RTC
    ) {
    }

    @Composable
    actual fun getppp(): Any {
        return ""
    }

    actual fun requestPermission(state: Any) {
    }

    actual fun getPermissionState(state: Any): LGPermissionState {
        return LGPermissionState.Denied
    }

}