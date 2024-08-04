package io.agora.live.livegame

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import io.agora.live.livegame.ui.HomeScreen
import io.agora.live.livegame.ui.theme.LiveGameTheme

@Composable
fun LiveGameApp(
    setSystemBarWhiteColor: (statusBar: Boolean, white: Boolean) -> Unit
) {
    LiveGameTheme {
        val isDarkMode = isSystemInDarkTheme()
        LaunchedEffect(isDarkMode) {
            setSystemBarWhiteColor(true, isDarkMode)
            setSystemBarWhiteColor(false, isDarkMode)
        }
        Navigator(HomeScreen)
    }
}