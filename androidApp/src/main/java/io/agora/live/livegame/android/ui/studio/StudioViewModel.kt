package io.agora.live.livegame.android.ui.studio

import android.view.SurfaceView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.agora.live.livegame.RTC
import io.agora.live.livegame.android.ui.list.RoomListViewModel
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.android.util.fromJsonToClass
import io.agora.live.livegame.bean.RoomInfo
import io.agora.live.livegame.log
import java.lang.NullPointerException
import java.net.URLDecoder

class StudioViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val KEY_ROOM_INFO = "ROOM_INFO"
    }

    lateinit var currentRoom: RoomInfo
    private val rtc = RTC()

    var viewState: MutableState<DataState<Any>> = mutableStateOf(DataState.None)
    val localUser = RoomListViewModel.liveUser
    private var amHost: Boolean = false

    init {
        savedStateHandle.get<String>(KEY_ROOM_INFO)?.also {
            URLDecoder.decode(it, "UTF-8").fromJsonToClass(RoomInfo::class.java)?.also { room ->
                currentRoom = room
            }
        }

        if (!this::currentRoom.isInitialized || localUser == null)
            viewState.value = DataState.Failure(NullPointerException("$KEY_ROOM_INFO is null"))
        else {
            amHost = localUser.id == currentRoom.ownerId

            "amHost:$amHost".log()

            loginRoom()
            rtc.attachCallback()
            rtc.enableLocalAudio(amHost)
            rtc.enableLocalVideo(amHost)
            if (amHost)
                rtc.publishStream(localUser.id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        rtc.stopPublishingStream()
        rtc.logoutRoom()

    }

    fun loginRoom() {
        localUser.also { rtc.loginRoom(currentRoom.id, it) }
    }

    fun previewView(view: SurfaceView) {
        rtc.previewCamera(view, if (amHost) "" else currentRoom.ownerId)
    }

}