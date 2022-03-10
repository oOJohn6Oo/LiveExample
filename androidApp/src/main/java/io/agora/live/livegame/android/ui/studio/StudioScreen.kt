package io.agora.live.livegame.android.ui.studio

import android.view.SurfaceView
import android.view.WindowInsetsController
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
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.insets.*
import io.agora.live.livegame.LocalData
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.android.util.overlayColor
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.bean.LiveUser
import io.agora.live.livegame.bean.RoomInfo
import io.agora.live.livegame.log

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewStudioScreen() {
    StudioScreen(
        messages = listOf(
            "AA: Aruba, Jamaga, wo I want to take ya",
            "AA: Bermuda, Bahama, come on pretty mama",
            "Kelago, Montego, wo I want to take you to the Kokomo",
        ), navBack = {}
    )
}

@ExperimentalComposeUiApi
@Composable
fun StudioScreen(
    studioViewModel: StudioViewModel = viewModel(),
    messages: List<String>,
    navBack: () -> Unit
) {

    // Exit when error happens in VM
    var viewState = studioViewModel.viewState.value
    if (viewState is DataState.Failure) {
        viewState = DataState.None
        navBack()
    }


    val liveUser: LiveUser = studioViewModel.localUser

    val currentRoom = studioViewModel.currentRoom

    ProvideWindowInsets {
        Box(Modifier.fillMaxSize()) {
            var bottomLayoutHeight by remember { mutableStateOf(0.dp) }
            val textFieldHeight = remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current

            // used to ensure a TextField is focused when showing keyboard
            val focusRequester = remember { FocusRequester() }
            val controller = LocalSoftwareKeyboardController.current

            // 游戏界面
//            AndroidView(factory = { context ->
//                WebView(context)
//            })
            // 直播画面
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                SurfaceView(it).apply {
                    studioViewModel.previewView(this)
                    setZOrderOnTop(false)
                }
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
                StudioTopLayout(roomInfo = currentRoom, liveUser)
                Spacer(Modifier.weight(1f))
                // 按钮
                StudioBottomLayout(modifier = Modifier.onSizeChanged {
                    bottomLayoutHeight = with(density) { it.height.toDp() }
                }, focusRequester, controller, navBack)
            }

            // 聊天区域

            LazyColumn(
                Modifier.fillMaxWidth().height(200.dp).padding(bottom = bottomLayoutHeight + 12.dp)
                    .navigationBarsWithImePadding()
                    .align(Alignment.BottomStart),
                //                Modifier.padding(rememberInsetsPaddingValues(bottomInsets, applyTop = false, additionalBottom = bottomLayoutHeight)).fillMaxWidth().height(400.dp).align(Alignment.BottomStart).background(color = Color.Red),
            ) {
                items(messages) { message ->
                    Text(message)
                }
            }

            BottomTextField(
                modifier = Modifier.fillMaxWidth()
                    .navigationBarsPadding().align(Alignment.BottomStart).alpha(if(LocalWindowInsets.current.ime.isVisible) 1f else 0f),
                textFieldHeight,
                focusRequester = focusRequester
            )
        }
    }
}

@Preview
@Composable
fun PreviewTopLayout() {
    StudioTopLayout(
        RoomInfo(
            "1",
            "name_1",
            "123",
            LocalData.localCover[0],
            System.currentTimeMillis()
        ), LiveUser("123", "name_123", LocalData.localAvatar[0])
    )
}

@Composable
fun StudioTopLayout(roomInfo: RoomInfo, owner: LiveUser) {
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
                AsyncImage(
                    model = owner.avatar,
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
            AsyncImage(
                model = LocalData.localAvatar[i],
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

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewStudioBottomLayout() {
    StudioBottomLayout(Modifier, FocusRequester(), LocalSoftwareKeyboardController.current){}
}

@ExperimentalComposeUiApi
@Composable
fun StudioBottomLayout(
    modifier: Modifier,
    focusRequester: FocusRequester,
    controller: SoftwareKeyboardController?,
    navBack: () -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val elementSize = 36.dp

        val overlayModifier = Modifier.background(
            color = MaterialTheme.overlayColor(0.12f),
            shape = CircleShape,
        ).size(elementSize)

        TextButton(
            onClick = {
                focusRequester.requestFocus()
                controller?.show()
            },
            modifier = Modifier.weight(1f).height(elementSize),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.overlayColor(0.12f),
                contentColor = MaterialTheme.colors.background
            ),
        ) {
            Text("Say something...", modifier = Modifier.fillMaxWidth())
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

        IconButton(onClick = navBack, modifier = overlayModifier) {
            Icon(Icons.Default.Close, contentDescription = "")
        }

    }
}

@Composable
fun BottomTextField(
    modifier: Modifier,
    textFieldHeight: MutableState<Dp>,
    focusRequester: FocusRequester
) {
    var value by remember { mutableStateOf("123123123") }

    val density = LocalDensity.current

    Box(
        modifier = modifier,
    ) {
        TextField(value,
            onValueChange = { value = it },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                .onFocusChanged {
                    "onFocusChanged-> ${it.hasFocus}".log()
                }
                .wrapContentSize().onSizeChanged {
                    textFieldHeight.value = with(density) { it.height.toDp() }
                })
    }
}