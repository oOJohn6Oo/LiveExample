package io.agora.live.livegame.android.ui

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.agora.live.livegame.*
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.android.util.dataStore
import io.agora.live.livegame.bean.LiveUser
import io.agora.live.livegame.bean.RoomInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import io.agora.live.livegame.log
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class RoomListViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object{
        lateinit var liveUser: LiveUser
        val gson: Gson = Gson().newBuilder().create()
    }

    private val channel: LiveChannel = LiveChannel.values()[savedStateHandle["sceneIndex"] ?: 0]

    private val rtm = RTM()
    val viewState: MutableState<DataState<List<RoomInfo>>> = mutableStateOf(DataState.Loading)

    init {
        val userKey = stringPreferencesKey("live_user")

        application.dataStore.data.map {
            it[userKey] ?: ""
        }.onEach {
            if (it.isBlank()) {
                val id = Random.nextInt(Int.MAX_VALUE)
                liveUser = LiveUser(
                    id.toString(),
                    "User-$id",
                    LocalData.localAvatar[id % LocalData.localAvatar.size]
                )
                application.dataStore.edit { edit ->
                    edit[userKey] = gson.toJson(liveUser)
                }
            } else {
                liveUser = gson.fromJson(it,LiveUser::class.java)
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            async {
                RTCAndroidTool.initRTC(application)
            }
            async {
                SyncAndroidTool.initSync(application, channel)
            }
            delay(2000)
            fetchRoomList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        "onCleared".log()
        RTCAndroidTool.destroy()
        SyncAndroidTool.destroy()
    }

    fun fetchRoomList() {
        callbackFlow {
            "fetch start ${Thread.currentThread().name}".log()
            val getRoomListCallback = object : BaseStateCallback<List<RoomInfo>> {
                override fun onSuccess(data: List<RoomInfo>) {
                    "fetch success".log()
                    trySend(DataState.Success(data))
                }

                override fun onFailure(exception: Throwable) {
                    "fetch onFailure:${exception.message}".log()
                    trySend(DataState.Failure(exception))
                }
            }
            rtm.getRoomList(getRoomListCallback)
            awaitClose {
                rtm.unregisterCallback(getRoomListCallback)
            }
        }.onEach { viewState.value = it }.launchIn(viewModelScope)

    }
}