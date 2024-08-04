package io.agora.live.livegame.ui.create

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.agora.live.livegame.data.bean.RoomInfo
import io.agora.live.livegame.shared.Platform
import io.agora.live.livegame.ui.LGPermissionState
import io.agora.live.livegame.ui.studio.StudioScreen
import io.agora.live.livegame.ui.util.DataState
import liveexample.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


data class CreateScreen(val studioNameList: List<String>): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val createViewModel: CreateRoomViewModel = rememberScreenModel { CreateRoomViewModel(studioNameList) }

        CreateContent(
            createViewModel = createViewModel,
            popBack = { navigator.pop() },
            nav2Studio = { navigator.replace(StudioScreen(it)) }
        )
    }

}

@Composable
private fun CreateContent(
    createViewModel: CreateRoomViewModel,
    popBack: () -> Unit,
    nav2Studio: (createdRoom: RoomInfo) -> Unit
) {
    val viewState = createViewModel.createState.value

    val pendingRoom = createViewModel.pendingRoomInfo.value

    if (viewState is DataState.Success) {
        createViewModel.createState.value = DataState.None
        nav2Studio(pendingRoom)
    } else if (viewState is DataState.Failure) {
        Platform.toast("Create error: ${viewState.exception.message}")
    }

    Box {

        val permissionsState = Platform.getppp()

        when(Platform.getPermissionState(permissionsState)){
            LGPermissionState.ShouldShowRationale ->{
                Platform.logD("lq", "permissionNotAvailableContent")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(Res.string.camera_permission_denied),
                        textAlign = TextAlign.Center
                    )
                }
            }
            LGPermissionState.Granted ->{
                // SurfaceView
                Platform.PreviewVideoView(
                    modifier = Modifier.fillMaxSize(),
                    rtc = createViewModel.rtc
                )
            }

            LGPermissionState.Denied -> {
                Platform.logD("lq", "permissionNotGrantedContent")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { Platform.requestPermission(permissionsState) }) {
                        Text(stringResource(Res.string.request_permissions))
                    }
                }
            }
        }

        Column(
            Modifier.safeDrawingPadding().padding(horizontal = 16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var animateStart by remember { mutableStateOf(false) }

            val animateRotateDegree by animateFloatAsState(
                if (animateStart) 360f else 0f,
                TweenSpec(if (animateStart) 500 else 0),
                finishedListener = {
                    if (animateStart) animateStart = false
                }
            )

            // Close Button
            IconButton(onClick = popBack, modifier = Modifier.align(Alignment.Start)) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(Res.string.nav_back),
                    tint = MaterialTheme.colors.background
                )
            }

            // 描述块
            Surface(
                contentColor = MaterialTheme.colors.background,
                color = Color.Transparent,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .background(
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium,
                    ).padding(12.dp)
            ) {
                // 直播间名称相关
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(Res.string.title_studio_name),
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pendingRoom.name,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        if (!animateStart) {
                            createViewModel.randomRoomName()
                            animateStart = true
                        }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(Res.string.desc_random_studio_name),
                            modifier = Modifier.rotate(animateRotateDegree)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            val permissionDeniedMessage = stringResource(Res.string.permissions_denied_in_need_of_live)

            // 底部"开始直播"按钮
            Button(
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                onClick = {
                    if (Platform.getPermissionState(permissionsState) == LGPermissionState.Granted)
                        createViewModel.createRoom()
                    else Platform.toast(permissionDeniedMessage)
                },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(text = stringResource(Res.string.start_live))
            }
        }
    }
}


@Preview
@Composable
private fun PreviewCreateScreen() {
//    CreateScreen(popBack = {}) {
//
//    }
}
