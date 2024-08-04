package io.agora.live.livegame.ui.studio

import LGWidget
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.agora.live.livegame.data.LocalData
import io.agora.live.livegame.data.bean.LiveUser
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.ui.util.DataState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import overlayColor
import kotlin.coroutines.coroutineContext


data class StudioScreen(val currentRoom: RoomInfo) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val vm: StudioViewModel = rememberScreenModel { StudioViewModel(currentRoom) }


        val scope = rememberCoroutineScope()

        val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

        val showOrHideSettingDialog: (Boolean) -> Unit = remember {
            {
                scope.launch { if(it ) bottomSheetState.show() else bottomSheetState.hide() }
            }
        }

        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = {
                SettingBottomDialogScreen(
                    showOrHideSettingDialog = showOrHideSettingDialog
                )
            },
            content = {
                StudioContent(
                    studioViewModel = vm,
                    msgList = vm.msgList,
                    showOrHideSettingDialog = showOrHideSettingDialog,
                ) {
                    navigator.pop()
                }
            }
        )
    }

}

@Composable
fun StudioContent(
    studioViewModel: StudioViewModel,
    msgList: List<String>,
    showOrHideSettingDialog: (Boolean) -> Unit,
    navBack: () -> Unit
) {

    // Exit when error happens in VM
    val viewState = studioViewModel.viewState.value
    if (viewState is DataState.Failure) {
        studioViewModel.viewState.value = DataState.None
        navBack()
    }


    val liveUser: LiveUser = studioViewModel.localUser

    val currentRoom = studioViewModel.currentRoom

    Box(Modifier.fillMaxSize()) {
        var bottomLayoutHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        // used to ensure a TextField is focused when showing keyboard
        val focusRequester = remember { FocusRequester() }
        val imeController = LocalSoftwareKeyboardController.current
        val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

        // 游戏界面
//            AndroidView(factory = { context ->
//                WebView(context)
//            })
        // 直播画面
        Platform.PlatformVideoView(
            modifier = Modifier.fillMaxSize(),
            owner = studioViewModel.amHost,
            streamId = studioViewModel.getStreamId(),
            rtc = studioViewModel.rtc
        )
        // 控制按钮层
        Column(
            modifier = Modifier.safeDrawingPadding().padding(horizontal = 16.dp)
        ) {
            // 顶部房间信息部分
            StudioTopLayout(roomInfo = currentRoom, liveUser)
            Spacer(Modifier.weight(1f))
            // 按钮
            StudioBottomLayout(
                modifier = Modifier.fillMaxWidth()
                    .onSizeChanged {
                        bottomLayoutHeight = with(density) { it.height.toDp() }
                    },
                focusRequester = focusRequester,
                showOrHideSettingDialog = showOrHideSettingDialog,
                onSendComment = {
                    studioViewModel.sendMessage(it)
                    imeController?.hide()
                    focusRequester.freeFocus()
                },
                navBack = navBack,
                isImeVisible = isImeVisible
            )
        }

        // 聊天区域
        LazyColumn(
            Modifier.fillMaxWidth().padding(bottom = bottomLayoutHeight + 12.dp)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .imePadding()
                .height(200.dp)
                .align(Alignment.BottomStart),
        ) {
            items(msgList) { message ->
                Text(message)
            }
        }
    }
}

@Composable
private fun StudioTopLayout(roomInfo: RoomInfo, owner: LiveUser) {
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
                KamelImage(
                    resource = asyncPainterResource(data = owner.avatar),
                    modifier = Modifier.size(26.dp).clip(CircleShape),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.size(4.dp))
                Text(roomInfo.name)
                Spacer(Modifier.size(4.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        // 房间成员信息
        for (i in 0..3) {
            KamelImage(
                resource = asyncPainterResource(data = LocalData.localAvatar[i]),
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
                Icon(Icons.Default.Person, contentDescription = "", modifier = Modifier.size(18.dp))
                Text("10")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewStudioBottomLayout() {
//    StudioBottomLayout(Modifier)
}

@Composable
private fun StudioBottomLayout(
    modifier: Modifier,
    isImeVisible: Boolean,
    focusRequester: FocusRequester,
    showOrHideSettingDialog: (Boolean) -> Unit,
    onSendComment: (String) -> Unit,
    navBack: () -> Unit
) {

    val elementSize = 36.dp

    val overlayModifier = Modifier.background(
        color = MaterialTheme.overlayColor(0.12f),
        shape = CircleShape,
    ).size(elementSize)

    val iconContainer = remember {
        movableContentOf {
            Row(modifier = Modifier.wrapContentSize()) {
                Spacer(Modifier.size(8.dp))
                IconButton(onClick = {}, modifier = overlayModifier) {
                    Icon(Icons.Default.Favorite, contentDescription = "", tint = Color(0xFFE7281E))
                }
                Spacer(Modifier.size(8.dp))
                IconButton(onClick = {
                    showOrHideSettingDialog(true)
                }, modifier = overlayModifier) {
                    Icon(Icons.Rounded.Settings, contentDescription = "")
                }

                Spacer(Modifier.size(8.dp))

                IconButton(onClick = navBack, modifier = overlayModifier) {
                    Icon(Icons.Default.Close, contentDescription = "")
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var inputText by remember {
                mutableStateOf("")
            }

            LGWidget.LGTextField(
                text = inputText,
                onValueChanged = { inputText = it },
                modifier = if (isImeVisible) {
                    Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp)
                } else {
                    Modifier.weight(1f).height(elementSize)
                }.background(MaterialTheme.overlayColor(0.12f), MaterialTheme.shapes.large)
                    .padding(horizontal = 8.dp).focusRequester(focusRequester),
                hideCount = true,
                maxLines = if (isImeVisible) Int.MAX_VALUE else 1,
                placeholder = "良言一句三冬暖，恶语一言六月寒",
                placeHolderStyle = MaterialTheme.typography.caption,
                contentAlignment = if (!isImeVisible) Alignment.CenterStart else Alignment.TopStart,
                focusRequester = focusRequester,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    val currentComment = inputText
                    if (currentComment.isNotBlank()) {
                        onSendComment(inputText)
                    }
                    inputText = ""
                }),
            )
            AnimatedVisibility(!isImeVisible) {
                iconContainer()
            }
        }
        AnimatedVisibility(isImeVisible) {
            Spacer(modifier = Modifier.size(8.dp))
            iconContainer()
        }
    }
}


@Preview
@Composable
fun PreviewStudioScreen() {
    StudioScreen(
        RoomInfo(
            "1",
            "name_1",
            "123",
            cover = "",
            createTime = 0L
        )
    )
}