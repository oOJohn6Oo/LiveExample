package io.agora.live.livegame.ui.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.agora.live.livegame.data.BaseStateCallback
import io.agora.live.livegame.data.LocalData
import io.agora.live.livegame.shared.RTC
import io.agora.live.livegame.shared.RTM
import io.agora.live.livegame.ui.list.RoomListViewModel
import io.agora.live.livegame.ui.util.DataState
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class CreateRoomViewModel(private val studioNameList: List<String>) : ScreenModel {
    private val rtm = RTM()
    val rtc = RTC()

    var pendingRoomInfo = mutableStateOf(randomRoom())
    val createState: MutableState<DataState<*>> = mutableStateOf(DataState.None)


    private fun randomRoom(): RoomInfo {
        return run {
            val randomName = studioNameList[Random.nextInt(Int.MAX_VALUE) % studioNameList.size]
            val randomId = Random.nextInt(100_000)
            RoomInfo(
                id = "live_john_$randomId",
                name = "${randomName}_$randomId",
                userId = RoomListViewModel.liveUser.id,
                cover = LocalData.localCover[Random.nextInt(Int.MAX_VALUE) % LocalData.localCover.size],
                createTime = Platform.currentTimeMillis
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
        }.launchIn(screenModelScope)

    }

//    fun setupLocalPreview(view: SurfaceView) {
//        rtc.enableLocalAudio(false)
//        rtc.enableLocalVideo(true)
//        rtc.previewCamera(view, "")
//    }
}