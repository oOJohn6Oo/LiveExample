package io.agora.live.livegame.ui.studio

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import io.agora.live.livegame.data.bean.LiveUser
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.shared.RTC
import io.agora.live.livegame.shared.RTM
import io.agora.live.livegame.ui.list.RoomListViewModel
import io.agora.live.livegame.ui.util.DataState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class StudioViewModel(val currentRoom: RoomInfo) : ScreenModel {

    companion object {
        const val KEY_ROOM_INFO = "ROOM_INFO"
    }

    val rtc = RTC()

    var viewState: MutableState<DataState<Any>> = mutableStateOf(DataState.None)
    val localUser: LiveUser = RoomListViewModel.liveUser
    val amHost = localUser.id == currentRoom.userId


    val msgList = mutableStateListOf<String>()

    val dataStreamId: Int
//
//    val msgFlow = MutableSharedFlow<String>(
//        replay = 0,
//        extraBufferCapacity = 100,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )

    init {

        Platform.logD("lq", "amHost:$amHost")

        loginRoom()
        dataStreamId = rtc.createDataStream()
        rtc.attachCallback()
        rtc.enableLocalAudio(amHost)
        rtc.enableLocalVideo(amHost)
        if (amHost) {
            rtc.publishStream(localUser.id)
        }

        RTC.onStreamMessage =  { uid, streamId, data ->
            if(data != null){
                msgList.add(data.toString())
//                msgFlow.tryEmit(data.toString())
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        rtc.stopPublishingStream()
        rtc.stopPreview()
        rtc.enableLocalAudio(false)
        rtc.enableLocalVideo(false)
        rtc.logoutRoom()
        if(amHost){
            RTM().deleteRoom(roomId = currentRoom.id)
        }
    }

    fun loginRoom() {
        localUser.also { rtc.loginRoom(currentRoom.id, it, amHost) }
    }

    fun sendMessage(msg:String){
        Platform.logD("lq", "sendMessage:$msg")
        rtc.sendMessage(msg, dataStreamId)
    }

    fun getStreamId() = currentRoom.userId

}