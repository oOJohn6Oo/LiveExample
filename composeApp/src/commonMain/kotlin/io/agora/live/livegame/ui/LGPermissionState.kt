package io.agora.live.livegame.ui

sealed class LGPermissionState {
    public data object ShouldShowRationale : LGPermissionState()
    data object Granted : LGPermissionState()
    data object Denied : LGPermissionState()
}