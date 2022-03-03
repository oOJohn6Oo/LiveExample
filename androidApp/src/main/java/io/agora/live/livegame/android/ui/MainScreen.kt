package io.agora.live.livegame.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import io.agora.live.livegame.android.R
import io.agora.live.livegame.bean.LiveScene

@Preview
@Composable
fun PreviewMainScreen(){
    MainScreen {  }
}

@Composable
fun MainScreen(nav2Rooms:(scene:String)->Unit) {
    // type 未设置的默认不展示
    val sceneNameList = LocalContext.current.resources.getStringArray(R.array.scenes_name)
    val sceneDescList = LocalContext.current.resources.getStringArray(R.array.scenes_desc)
    val scenes = mutableListOf<LiveScene>()

    sceneNameList.forEachIndexed { index, s ->
        scenes.add(LiveScene(s, sceneDescList[index]))
        scenes.add(LiveScene(s, sceneDescList[index]))
        scenes.add(LiveScene(s, sceneDescList[index]))
    }

    ProvideWindowInsets(false) {
        val lazeListState = rememberLazyListState()

        val inset = LocalWindowInsets.current.systemBars
        Column {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = lazeListState.elevation,
                contentPadding = rememberInsetsPaddingValues(
                    inset,
                    applyStart = false,
                    applyTop = true,
                    applyEnd = false,
                    applyBottom = false
                ),
            ) {
                Text(
                    stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = MaterialTheme.colors.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                state = lazeListState,
                contentPadding = rememberInsetsPaddingValues(inset, applyTop = false)
            ) {
                items(scenes) { scene ->
                    HomeBadgeItem(scene, nav2Rooms)
                }
            }
        }
    }
}

val LazyListState.elevation
    get() = if (firstVisibleItemIndex == 0) minOf(
        firstVisibleItemScrollOffset.dp,
        AppBarDefaults.TopAppBarElevation
    )
    else AppBarDefaults.TopAppBarElevation

@Composable
fun HomeBadgeItem(scene: LiveScene, nav2Rooms: (scene: String) -> Unit) {
    Box(
        Modifier.fillMaxWidth().wrapContentHeight()
            .padding(horizontal = 24f.dp, vertical = 12f.dp)
            .clickable{nav2Rooms(scene.name)},
    ) {
        Image(
            painterResource(R.drawable.app_banner_live_game),
            "",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center
        )
        Text(
            text = scene.name,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.align(BiasAlignment(0.6f, -0.28f))
                .wrapContentSize(),
        )
        Text(
            text = scene.desc,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier.align(BiasAlignment(0.6f, 0.2f))
                .wrapContentSize(),
        )
    }
}