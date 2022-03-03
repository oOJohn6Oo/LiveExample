package io.agora.live.livegame.android.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
import io.agora.live.livegame.android.ui.theme.BtnEndColor
import io.agora.live.livegame.android.ui.theme.BtnStartColor
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.bean.RoomInfo


@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewRoomListScreen() {
    val testData = listOf(
        RoomInfo("id1", "name1", "ownerId1"),
        RoomInfo("id2", "name2", "ownerId2"),
        RoomInfo("id3", "name3", "ownerId3")
    )
    RoomListScreen(testData, {}) {}
}

@ExperimentalFoundationApi
@Composable
fun RoomListScreen(
    roomInfoList: List<RoomInfo>,
    nav2Room: (roomInfo: RoomInfo) -> Unit,
    nav2Create: () -> Unit,
) {
    ProvideWindowInsets(false) {

        val density = LocalDensity.current

        val lazeListState = rememberLazyListState()

        val inset = LocalWindowInsets.current.systemBars

        var appBarHeight by remember { mutableStateOf(0.dp) }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                state = lazeListState,
                cells = GridCells.Fixed(2),
                contentPadding = rememberInsetsPaddingValues(
                    inset,
                    applyTop = false,
                    additionalTop = appBarHeight + 12.dp,
                    additionalStart = 12.dp,
                    additionalEnd = 12.dp,
                ),
//                contentPadding = rememberInsetsPaddingValues(
//                    inset,
//                    applyTop = false,
//                    additionalTop = appBarHeight + 12.dp,
//                    additionalStart = 12.dp,
//                    additionalEnd = 12.dp,
//                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(roomInfoList) { roomInfo ->
                    RoomItem(roomInfo, nav2Room)
                }
            }

            TopAppBar(
                modifier = Modifier.onSizeChanged { size ->
                    appBarHeight = with(density) { size.height.toDp() }
                },
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


            val fabSource by remember { mutableStateOf(MutableInteractionSource()) }
            val fabElevation = fabSource.collectIsPressedAsState()

            Surface(
                modifier = Modifier.systemBarsPadding(
                    applyTop = false,
                    applyStart = false,
                    additionalEnd = 12.dp,
                ).size(56.dp)
                    .align(Alignment.BottomEnd).clickable(
                        interactionSource = fabSource,
                        indication = null,
                        onClick = nav2Create
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
                        ), contentDescription = "",
                    )
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.create_room),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewRoomItem() {
    RoomItem(RoomInfo("a1b1c1", "哈哈", "user-1231")) {}
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
//            .hoverable(interactionSource = source, true),
        elevation = elevationValue
    ) {
        Box {
            Image(
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(12.dp).fillMaxSize().clip(MaterialTheme.shapes.small),
                painter = ColorPainter(MaterialTheme.colors.secondary)
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