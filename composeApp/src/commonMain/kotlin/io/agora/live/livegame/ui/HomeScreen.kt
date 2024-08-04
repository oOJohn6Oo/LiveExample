package io.agora.live.livegame.ui

import LGWidget
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import elevation
import insetsBottomSafeDrawing
import insetsTopSafeDrawing
import io.agora.live.livegame.data.LocalData
import io.agora.live.livegame.data.bean.LiveScene
import io.agora.live.livegame.ui.list.RoomListScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import liveexample.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data object HomeScreen: Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            HomeContent { navigator.push(RoomListScreen(it)) }
        }
    }

}

@Composable
private fun HomeContent(nav2RoomList: (scene: Int) -> Unit) {
    // type 未设置的默认不展示
    val sceneNameList = stringArrayResource(Res.array.scenes_name)
    val sceneDescList = stringArrayResource(Res.array.scenes_desc)
    val scenes = mutableListOf<LiveScene>()

    sceneNameList.forEachIndexed { index, s ->
        scenes.add(LiveScene(index, s, sceneDescList[index]))
    }

    val lazeListState = rememberLazyListState()

    Box {
        val density = LocalDensity.current
        var appBarHeight by remember { mutableStateOf(0.dp) }

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            state = lazeListState,
            contentPadding = insetsBottomSafeDrawing(false).add(
                WindowInsets(top = appBarHeight)
            ).asPaddingValues()
        ) {
            items(scenes) { scene ->
                HomeBadgeItem(scene, nav2RoomList)
            }
        }

        LGWidget.LiftableTopAppBar(
            modifier = Modifier.onSizeChanged { size ->
                appBarHeight = with(density) { size.height.toDp() }
            },
            elevation = lazeListState.elevation,
            contentPadding = insetsTopSafeDrawing().asPaddingValues(),
            title = stringResource(Res.string.app_name)
        )
    }
}

@Composable
private fun HomeBadgeItem(scene: LiveScene, nav2RoomList: (sceneIndex: Int) -> Unit) {

    Surface(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.large,
        elevation = 4.dp,
    ) {
        Box {
            KamelImage(
                resource = asyncPainterResource(LocalData.bannerURL),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
                    .height(180.dp).clickable { nav2RoomList(scene.index) },
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            )
            Text(
                text = scene.name,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(BiasAlignment(0.6f, -0.28f))
                    .wrapContentSize(),
            )
            Text(
                text = scene.desc,
                style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 14.sp),
                modifier = Modifier.align(BiasAlignment(0.6f, 0.2f))
                    .wrapContentSize(),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    HomeContent { }
}