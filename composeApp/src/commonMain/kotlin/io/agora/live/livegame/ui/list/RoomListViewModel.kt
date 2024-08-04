package io.agora.live.livegame.ui.list

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.agora.live.livegame.di.dataStore
import io.agora.live.livegame.LiveChannel
import io.agora.live.livegame.data.BaseStateCallback
import io.agora.live.livegame.data.LocalData
import io.agora.live.livegame.data.bean.LiveUser
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.shared.RTC
import io.agora.live.livegame.shared.RTM
import io.agora.live.livegame.ui.util.DataState
import io.agora.live.livegame.ui.util.LGJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import liveexample.composeapp.generated.resources.*
import kotlin.random.Random

class RoomListViewModel(sceneIndex: Int) : ScreenModel {

    private val channel: LiveChannel = LiveChannel.entries[sceneIndex]

    private val rtm = RTM()

    private var currentList = listOf<RoomInfo>()
    val viewState = mutableStateOf(ListUiState())

    init {
        val userKey = stringPreferencesKey("live_user")

        dataStore.data.map {
            it[userKey] ?: ""
        }.onEach {
            if (it.isBlank()) {
                val id = Random.nextInt(Int.MAX_VALUE)
                liveUser = LiveUser(
                    id.toString(),
                    "User-$id",
                    LocalData.localAvatar[id % LocalData.localAvatar.size]
                )
                dataStore.edit { edit ->
                    edit[userKey] = Json.encodeToString(liveUser)
                }
            } else {
                Json.decodeFromString<LiveUser>(it).also { user->
                    liveUser = user
                }
            }
        }.launchIn(screenModelScope)

        screenModelScope.launch(Dispatchers.IO) {
            RTM.init(channel){
                fetchRoomList()
            }
        }
    }

    private var dismissJob: Job? = null

    private fun dismissToastAfterDelay(delayMillis: Long = 3000){
        dismissJob?.cancel()
        dismissJob = screenModelScope.launch {
            delay(delayMillis)
            viewState.value = viewState.value.copy(message = null)
        }
    }


    override fun onDispose() {
        super.onDispose()
        Platform.logD("lq", "onCleared")
        RTC.destroy()
        RTM.destroy()
    }

    fun fetchRoomList() {
        callbackFlow {
            Platform.logD("lq", "fetch start ${Platform.currentThreadName}")
            val getRoomListCallback = object : BaseStateCallback<List<String>> {
                override fun onSuccess(data: List<String>) {
                    val res:List<RoomInfo> = data.mapNotNull {
                        try {
                            LGJson.decodeFromString<RoomInfo>(it)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    currentList = res
                    trySend(
                        ListUiState(
                            uiState = DataState.Success(currentList),
                            message = null,
                            isRefreshing = false
                        )
                    )
                }

                override fun onFailure(exception: Throwable) {
                    Platform.logD("lq", "fetch onFailure:${exception.message}")
                    trySend(
                        ListUiState(
                            uiState = DataState.Success(currentList),
                            message = Res.string.no_data_available,
                            isRefreshing = false
                        )
                    )
                    dismissToastAfterDelay()
                }
            }
            rtm.getRoomList(getRoomListCallback)
            awaitClose {
                rtm.unregisterCallback(getRoomListCallback)
            }
        }.onEach { viewState.value = it }.launchIn(screenModelScope)

    }

    companion object {
        lateinit var liveUser: LiveUser
    }
}