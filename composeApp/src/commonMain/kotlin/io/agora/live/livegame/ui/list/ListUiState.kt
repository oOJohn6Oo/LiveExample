package io.agora.live.livegame.ui.list

import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.ui.util.DataState
import org.jetbrains.compose.resources.StringResource

data class ListUiState(
    val uiState: DataState<List<RoomInfo>> = DataState.None,
    val message: StringResource? = null,
    val isRefreshing: Boolean = true,
)
