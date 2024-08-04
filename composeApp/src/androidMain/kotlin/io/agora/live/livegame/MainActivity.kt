package io.agora.live.livegame

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // EnforceXXX is not supported below API 29
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        val controller = WindowCompat.getInsetsController(window, window.decorView)
        setContent {
            LiveGameApp{ statusBar, white->
                if(statusBar){
                    controller.isAppearanceLightStatusBars = !white
                }else{
                    controller.isAppearanceLightNavigationBars = !white
                }
            }
        }
    }
}