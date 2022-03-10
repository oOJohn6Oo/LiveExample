package io.agora.live.livegame.android.ui.create

import android.view.SurfaceView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.agora.live.livegame.BaseStateCallback
import io.agora.live.livegame.LocalData
import io.agora.live.livegame.RTC
import io.agora.live.livegame.RTM
import io.agora.live.livegame.android.ui.list.RoomListViewModel
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.bean.RoomInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class CreateRoomViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val rtm = RTM()
    private val rtc = RTC()

    private val studioNameList: Array<String> =
        savedStateHandle.get<Array<String>>("studioNameList") ?: arrayOf()

    var pendingRoomInfo = mutableStateOf(randomRoom())
    val createState: MutableState<DataState<*>> = mutableStateOf(DataState.None)


    private fun randomRoom(): RoomInfo {
        return run {
            val randomName = studioNameList[Random.nextInt(Int.MAX_VALUE) % studioNameList.size]
            val randomId = Random.nextInt(100_000)
            RoomInfo(
                "live_john_$randomId",
                "${randomName}_$randomId",
                RoomListViewModel.liveUser.id,
                LocalData.localCover[Random.nextInt(Int.MAX_VALUE) % LocalData.localCover.size],
                System.currentTimeMillis()
            )
        }
    }

    fun randomRoomName() {
        pendingRoomInfo.value = randomRoom()
    }

    fun createRoom() {

        callbackFlow {
            val callback = object : BaseStateCallback<Unit> {
                override fun onSuccess(data: Unit) {
                    trySend(DataState.Success(data))
                }

                override fun onFailure(exception: Throwable) {
                    trySend(DataState.Failure(exception))
                }
            }
            rtm.createRoom(pendingRoomInfo.value, callback)
            awaitClose { rtm.unregisterCallback(callback) }
        }.onEach {
            createState.value = it
        }.launchIn(viewModelScope)

    }

    fun setupLocalPreview(view: SurfaceView) {
        rtc.enableLocalAudio(false)
        rtc.enableLocalVideo(true)
        rtc.previewCamera(view, "")
    }
}