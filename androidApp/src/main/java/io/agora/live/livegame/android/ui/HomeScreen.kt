package io.agora.live.livegame.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import io.agora.live.livegame.LocalData
import io.agora.live.livegame.android.R
import io.agora.live.livegame.bean.LiveScene

@Preview
@Composable
fun PreviewMainScreen() {
    HomeScreen { }
}

@Composable
fun HomeScreen(nav2RoomList: (scene: Int) -> Unit) {
    // type 未设置的默认不展示
    val sceneNameList = LocalContext.current.resources.getStringArray(R.array.scenes_name)
    val sceneDescList = LocalContext.current.resources.getStringArray(R.array.scenes_desc)
    val scenes = mutableListOf<LiveScene>()

    sceneNameList.forEachIndexed { index, s ->
        scenes.add(LiveScene(index, s, sceneDescList[index]))
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
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                state = lazeListState,
                contentPadding = rememberInsetsPaddingValues(inset, applyTop = false)
            ) {
                items(scenes) { scene ->
                    HomeBadgeItem(scene, nav2RoomList)
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
fun HomeBadgeItem(scene: LiveScene, nav2RoomList: (sceneIndex: Int) -> Unit) {

    Surface (
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(horizontal = 24f.dp, vertical = 12f.dp),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.large,
        elevation = 4.dp,
    ) {
        Box {
            AsyncImage(
                model = LocalData.bannerURL,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
                    .height(180.dp).clickable { nav2RoomList(scene.index) },
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            )
            Text(
                text = scene.name,
                style = TextStyle(color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.align(BiasAlignment(0.6f, -0.28f))
                    .wrapContentSize(),
            )
            Text(
                text = scene.desc,
                style = TextStyle(color = Color.Black, fontSize = 14.sp),
                modifier = Modifier.align(BiasAlignment(0.6f, 0.2f))
                    .wrapContentSize(),
            )
        }
    }
}

@Composable
fun LiftableTopAppBar(
    modifier: Modifier = Modifier,
    lazeListState: LazyListState,
    contentPadding: PaddingValues,
    title: String
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        elevation = lazeListState.elevation,
        contentPadding = contentPadding,
    ) {
        Text(
            title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}