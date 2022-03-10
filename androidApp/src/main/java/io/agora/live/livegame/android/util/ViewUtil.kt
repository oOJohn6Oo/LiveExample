package io.agora.live.livegame.android.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import io.agora.live.livegame.android.ui.list.RoomListViewModel


fun <T> String.fromJsonToClass(clazz:Class<T>) : T?= RoomListViewModel.moshi.adapter(clazz).fromJson(this)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ds_user")

fun Configuration.isNightMode() =
    uiMode or Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun isNightMode() =
    Resources.getSystem().configuration.uiMode or Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@Composable
fun MaterialTheme.overlayColor(@FloatRange(from = 0.0, to = 1.0) alpha:Float = 0.2f) = colors.onBackground.copy(alpha = alpha)

fun Modifier.systemBarsPadding(
    applyStart: Boolean = true,
    applyTop: Boolean = true,
    applyEnd: Boolean = true,
    applyBottom: Boolean = true,
    additionalStart: Dp = 0.dp,
    additionalTop: Dp = 0.dp,
    additionalEnd: Dp = 0.dp,
    additionalBottom: Dp = 0.dp,
): Modifier = composed {
    padding(
        rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyStart = applyStart,
            applyTop = applyTop,
            applyEnd = applyEnd,
            applyBottom = applyBottom,
            additionalStart = additionalStart,
            additionalTop = additionalTop,
            additionalEnd = additionalEnd,
            additionalBottom = additionalBottom,
        )
    )
}