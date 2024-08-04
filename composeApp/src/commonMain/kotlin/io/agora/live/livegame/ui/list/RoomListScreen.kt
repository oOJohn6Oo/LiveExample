@file:OptIn(ExperimentalMaterialApi::class)

package io.agora.live.livegame.ui.list

import LGWidget
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bottomSafeDrawing
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import elevation
import insetsTopSafeDrawing
import io.agora.live.livegame.data.LocalData
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.shared.RTC
import io.agora.live.livegame.ui.LGPermissionState
import io.agora.live.livegame.ui.create.CreateScreen
import io.agora.live.livegame.ui.studio.StudioScreen
import io.agora.live.livegame.ui.theme.BtnEndColor
import io.agora.live.livegame.ui.theme.BtnStartColor
import io.agora.live.livegame.ui.util.DataState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import liveexample.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewRoomListScreen() {
//    RoomListContent("test", nav2Room = {}) {}
}

data class RoomListScreen(val sceneIndex: Int) : Screen {
    @Composable
    override fun Content() {

//        val objects by screenModel.objects.collectAsState()
        val roomListViewModel = rememberScreenModel{
            RoomListViewModel(sceneIndex)
        }
        val navigator = LocalNavigator.currentOrThrow

        val nameList = stringArrayResource(Res.array.studio_name_list)

        val permissionState = Platform.getppp()
        if(Platform.getPermissionState(permissionState) == LGPermissionState.Granted){
            RTC.init()
        }
        RoomListContent(
            sceneName = stringArrayResource(Res.array.scenes_name)[sceneIndex],
            roomListState = roomListViewModel.viewState.value,
            nav2Room = {roomInfo: RoomInfo ->
                if(Platform.getPermissionState(permissionState) == LGPermissionState.Granted){
                    navigator.push(StudioScreen(roomInfo))
                }else{
                    Platform.requestPermission(permissionState)
                }
            },
            nav2Create = {
                navigator.push(CreateScreen(nameList))
            },
            refreshRoomList = {
                roomListViewModel.fetchRoomList()
            })
    }
}

@Composable
private fun RoomListContent(
    sceneName: String,
    roomListState: ListUiState,
    refreshRoomList: () -> Unit,
    nav2Room: (roomInfo: RoomInfo) -> Unit,
    nav2Create: () -> Unit,
) {
    val density = LocalDensity.current

    var appBarHeight by remember { mutableStateOf(0.dp) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = roomListState.isRefreshing,
        onRefresh = refreshRoomList
    )

    roomListState.message?.also {
        LGWidget.LGToast(
            modifier = Modifier
                .background(MaterialTheme.colors.onSurface, MaterialTheme.shapes.large)
                .padding(12.dp),
            msg = stringResource(it)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
    ) {

        val lazeListState = rememberLazyGridState()

        when (roomListState.uiState) {

            // Content
            is DataState.Success -> {
                val roomInfoList: List<RoomInfo> = roomListState.uiState.data

                if (roomInfoList.isEmpty()) {
                    EmptyList(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState, enabled = true)
                            .verticalScroll(rememberScrollState()),
                    )
                } else {
                    ContentList(
                        modifier = Modifier.fillMaxSize()
                            .pullRefresh(pullRefreshState, enabled = true),
                        lazeListState,
                        appBarHeight,
                        roomInfoList,
                        nav2Room
                    )
                }
            }

            else -> {

            }
        }

        LGWidget.LiftableTopAppBar(
            modifier = Modifier.onSizeChanged { size ->
                appBarHeight = with(density) { size.height.toDp() }
            },
            elevation = lazeListState.elevation,
            contentPadding = insetsTopSafeDrawing().asPaddingValues(),
            title = sceneName
        )

        GradientFAB(
            modifier = Modifier.align(Alignment.BottomEnd)
                .bottomSafeDrawing().padding(end = 16.dp)
                .padding(bottom = 32.dp).size(56.dp),
            nav2Create
        )

        PullRefreshIndicator(
            refreshing = roomListState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = appBarHeight),
            contentColor = MaterialTheme.colors.primary
        )
    }

}

@Composable
private fun EmptyList(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = stringResource(Res.string.no_data_available),
            modifier = Modifier.size(120.dp)
        )
        Text(stringResource(Res.string.no_data_available))
    }
}

@Composable
private fun ContentList(
    modifier:Modifier = Modifier,
    lazeListState: LazyGridState,
    appBarHeight: Dp,
    roomInfoList: List<RoomInfo>,
    nav2Room: (roomInfo: RoomInfo) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = lazeListState,
        columns = GridCells.Adaptive(160.dp),
        contentPadding = WindowInsets.safeDrawing.add(
            WindowInsets(
                top = appBarHeight,
                left = 16.dp,
                right = 16.dp
            )
        ).asPaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(roomInfoList) { roomInfo ->
            RoomItem(roomInfo, nav2Room)
        }
    }
}

@Preview
@Composable
fun PreviewRoomItem() {
    RoomItem(RoomInfo("a1b1c1", "哈哈", "user-1231", LocalData.localCover[0], Platform.currentTimeMillis)) {}
}

@Composable
fun RoomItem(roomInfo: RoomInfo, nav2Room: (roomInfo: RoomInfo) -> Unit) {
    val source by remember { mutableStateOf(MutableInteractionSource()) }

    val isPressed = source.collectIsPressedAsState()

    val elevationValue by animateDpAsState(if (isPressed.value) 16.dp else 2.dp)

    Card(
        shape = MaterialTheme.shapes.large,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.aspectRatio(1f)
            .clickable(interactionSource = source, indication = null) { nav2Room(roomInfo) },
        elevation = elevationValue
    ) {
        Box {
            KamelImage(
                resource = asyncPainterResource(roomInfo.cover),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(12.dp).fillMaxSize().clip(MaterialTheme.shapes.small),
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.display_id, roomInfo.id),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                )
                Text(
                    text = stringResource(Res.string.display_name, roomInfo.name),
                    style = TextStyle(fontSize = 14.sp),
                )
            }
        }
    }
}


/**
 * 渐变色 FAB
 */
@Composable
fun GradientFAB(modifier: Modifier = Modifier, onclick: () -> Unit) {
    val fabSource by remember { mutableStateOf(MutableInteractionSource()) }
    val fabElevation = fabSource.collectIsPressedAsState()

    Surface(
        modifier = modifier.clickable(
            interactionSource = fabSource,
            indication = null,
            onClick = onclick
        ),
        shape = CircleShape,
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.background,
        elevation = if (fabElevation.value) 6.dp else 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                BrushPainter(
                    Brush.linearGradient(
                        listOf(
                            BtnStartColor,
                            BtnEndColor
                        )
                    )
                ),
                contentDescription = "",
            )
            Icon(
                Icons.Rounded.Add,
                contentDescription = stringResource(Res.string.create_room),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}