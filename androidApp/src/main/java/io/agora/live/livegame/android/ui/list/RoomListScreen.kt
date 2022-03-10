package io.agora.live.livegame.android.ui.list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import io.agora.live.livegame.LocalData
import io.agora.live.livegame.android.R
import io.agora.live.livegame.android.theme.BtnEndColor
import io.agora.live.livegame.android.theme.BtnStartColor
import io.agora.live.livegame.android.ui.LiftableTopAppBar
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.bean.RoomInfo
import io.agora.live.livegame.log


@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewRoomListScreen() {
    RoomListScreen("HAHA", nav2Room = {}) {}
}

@ExperimentalFoundationApi
@Composable
fun RoomListScreen(
    sceneName: String,
    roomListViewModel: RoomListViewModel = viewModel(),
    nav2Room: (roomInfo: RoomInfo) -> Unit,
    nav2Create: () -> Unit,
) {
    "RoomListScreen".log()

    val roomListState = roomListViewModel.viewState.value

    ProvideWindowInsets(false) {

        val density = LocalDensity.current

        val insets = LocalWindowInsets.current.systemBars

        var appBarHeight by remember { mutableStateOf(0.dp) }

        Box(modifier = Modifier.fillMaxSize()) {

            val lazeListState = rememberLazyListState()

            when (roomListState) {

                // Content
                is DataState.Success, is DataState.Failure -> {
                    val roomInfoList: List<RoomInfo> =
                        if (roomListState is DataState.Success) roomListState.data else listOf()
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize().background(color = Color.Red),
                        state = lazeListState,
                        cells = GridCells.Fixed(2),
                        contentPadding = rememberInsetsPaddingValues(
                            insets,
                            applyTop = false,
                            additionalTop = appBarHeight + 12.dp,
                            additionalStart = 12.dp,
                            additionalEnd = 12.dp,
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(roomInfoList) { roomInfo ->
                            RoomItem(roomInfo, nav2Room)
                        }
                    }
                }
                // Loading
                is DataState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {}
            }

            LiftableTopAppBar(
                modifier = Modifier.onSizeChanged { size ->
                    appBarHeight = with(density) { size.height.toDp() }
                }, lazeListState, contentPadding = rememberInsetsPaddingValues(
                    insets,
                    applyTop = true,
                    applyBottom = false,
                ), sceneName
            )

            GradientFAB(modifier = Modifier.align(Alignment.BottomEnd), nav2Create)

        }

    }
}

@Composable
fun RoomListContent() {

}

@Preview
@Composable
fun PreviewRoomItem() {
    RoomItem(RoomInfo("a1b1c1", "哈哈", "user-1231", LocalData.localCover[0], System.currentTimeMillis())) {}
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
            AsyncImage(
                model = roomInfo.cover,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(12.dp).fillMaxSize().clip(MaterialTheme.shapes.small),
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.display_id, roomInfo.id),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                )
                Text(
                    text = stringResource(R.string.display_name, roomInfo.name),
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
        modifier = modifier.systemBarsPadding(
            applyTop = false,
            applyStart = false,
            additionalBottom = 12.dp,
            additionalEnd = 12.dp,
        ).size(56.dp).clickable(
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
                contentDescription = stringResource(R.string.create_room),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}