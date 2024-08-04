package io.agora.live.livegame.shared

import android.Manifest
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dataStoreFileName
import doCreateDataStore
import io.agora.live.livegame.LiveGameApplication
import io.agora.live.livegame.ui.LGPermissionState

actual object Platform {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val currentTimeMillis
        get() = System.currentTimeMillis()

    actual val currentThreadName
        get() = Thread.currentThread().name

    actual fun toast(message: String) {
        Toast.makeText(LiveGameApplication.instance, message, Toast.LENGTH_SHORT).show()
    }

    actual fun createDataStore(): DataStore<Preferences> {
        return doCreateDataStore(
            producePath = { LiveGameApplication.instance.filesDir.resolve(dataStoreFileName).absolutePath }
        )
    }

    actual fun logD(tag: String, message: String) {
        Log.d(tag, message)
    }

    @Composable
    actual fun PlatformVideoView(modifier: Modifier, owner: Boolean, streamId: String, rtc: RTC) {
        AndroidView(modifier = modifier, factory = {
            SurfaceView(it).apply {
                rtc.previewCamera(this, if(owner) "" else streamId)
                rtc.publishStream(streamId)
                setZOrderOnTop(false)
            }
        })
    }


    @Composable
    actual fun PreviewVideoView(modifier: Modifier, rtc: RTC) {
        AndroidView(modifier = modifier, factory = {
            SurfaceView(it).apply {
                rtc.enableLocalAudio(false)
                rtc.enableLocalVideo(true)
                rtc.previewCamera(this, "")
                setZOrderOnTop(false)
            }
        })
    }
    @OptIn(ExperimentalPermissionsApi::class)
    actual fun requestPermission(state: Any){
        if(state is MultiplePermissionsState){
            state.launchMultiplePermissionRequest()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    actual fun getppp():Any = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    @OptIn(ExperimentalPermissionsApi::class)
    actual fun getPermissionState(state: Any): LGPermissionState{
        if(state is MultiplePermissionsState){
            return if(state.allPermissionsGranted) LGPermissionState.Granted
            else if(state.shouldShowRationale) LGPermissionState.ShouldShowRationale
            else LGPermissionState.Denied
        }
        return LGPermissionState.Granted
    }

}