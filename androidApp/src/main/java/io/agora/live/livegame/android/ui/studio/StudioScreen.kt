package io.agora.live.livegame.android.ui

import android.view.SurfaceView
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.ProvideWindowInsets
import io.agora.live.livegame.android.R
import io.agora.live.livegame.android.util.overlayColor
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.log

@Preview
@Composable
fun PreviewStudioScreen() {
    StudioScreen(
        listOf(
            "AA: Aruba, Jamaga, wo I want to take ya",
            "AA: Bermuda, Bahama, come on pretty mama",
            "Kelago, Montego, wo I want to take you to the Kokomo",
        )
    )
}

@Composable
fun StudioScreen(messages: List<String>) {
    "StudioScreen".log()
    ProvideWindowInsets {
        Box {
            // 游戏界面
            AndroidView(factory = { context ->
                WebView(context)
            })
            // 直播画面
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                SurfaceView(it)
            })
            // 控制按钮层
            Column(
                modifier = Modifier.systemBarsPadding(
                    additionalStart = 12.dp,
                    additionalTop = 12.dp,
                    additionalEnd = 12.dp,
                    additionalBottom = 12.dp,
                )
            ) {
                // 顶部房间信息部分
                StudioTopLayout("sdf")
                Spacer(Modifier.weight(1f))
                // 聊天区域
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(messages) { message ->
                        Text(message)
                    }
                }
                Spacer(Modifier.size(12.dp))
                // 按钮
                StudioBottomLayout()
            }
        }
    }
}

@Preview
@Composable
fun PreviewTopLayout() {
    StudioTopLayout("糖糖")
}

@Composable
fun StudioTopLayout(studioName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 房间名称
        Surface(
            color = Color.Transparent,
            modifier = Modifier.background(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                shape = CircleShape
            ).padding(4.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.app_banner_livepk),
                    modifier = Modifier.size(26.dp).clip(CircleShape),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.size(4.dp))
                Text(studioName)
            }
        }

        Spacer(Modifier.weight(1f))

        // 房间成员信息
        for (i in 0..3) {
            Image(
                painter = painterResource(R.drawable.app_banner_livepk),
                modifier = Modifier.size(26.dp).clip(CircleShape),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.size(4.dp))
        }

        // 房间人数
        Surface(
            color = Color.Transparent,
            modifier = Modifier.background(
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                shape = CircleShape
            ).padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "")
                Text("10")
            }
        }
    }
}

@Preview
@Composable
fun PreviewStudioBottomLayout() {
    StudioBottomLayout()
}

@Composable
fun StudioBottomLayout() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val overlayModifier = Modifier.background(
            color = MaterialTheme.overlayColor(0.12f),
            shape = CircleShape,
        )

        IconButton(onClick = {}, modifier = overlayModifier.weight(1f)) {
            Text("Say something")
        }
        Spacer(Modifier.size(8.dp))
        IconButton(onClick = {}, modifier = overlayModifier) {
            Icon(Icons.Default.Favorite, contentDescription = "")
        }
        Spacer(Modifier.size(8.dp))
        IconButton(onClick = {}, modifier = overlayModifier) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = "")
        }
        Spacer(Modifier.size(8.dp))
        IconButton(onClick = {}, modifier = overlayModifier) {
            Icon(Icons.Rounded.Settings, contentDescription = "")
        }

        Spacer(Modifier.size(8.dp))

        IconButton(onClick = {}, modifier = overlayModifier) {
            Icon(Icons.Default.Close, contentDescription = "")
        }

    }
}