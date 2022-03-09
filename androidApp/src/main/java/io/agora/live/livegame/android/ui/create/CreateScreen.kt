package io.agora.live.livegame.android.ui.create

import android.Manifest
import android.view.SurfaceView
import android.widget.Toast
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.permissions.*
import io.agora.live.livegame.android.R
import io.agora.live.livegame.android.util.DataState
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.bean.RoomInfo
import io.agora.live.livegame.log

@ExperimentalPermissionsApi
@Preview
@Composable
fun PreviewCreateScreen() {
    CreateScreen(popBack = {}) {

    }
}

@ExperimentalPermissionsApi
@Composable
fun CreateScreen(
    roomViewModel: CreateRoomViewModel = viewModel(),
    popBack: () -> Unit,
    nav2Studio: (createdRoom: RoomInfo) -> Unit
) {
    "CreateScreen".log()

    val currentContext = LocalContext.current

    val pendingRoomInfo = roomViewModel.pendingRoomInfo.value

    val createState = roomViewModel.createState.value

    if (createState is DataState.Success) {
        roomViewModel.createState.value = DataState.None
        nav2Studio(pendingRoomInfo)
    } else if (createState is DataState.Failure) {
        Toast.makeText(
            currentContext,
            "Create error: ${createState.exception.message}",
            Toast.LENGTH_SHORT
        ).show()
    }

    ProvideWindowInsets {

        Box {

            val permissionsState = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            )

            LaunchedEffect("permissionsState") {
                if (!permissionsState.allPermissionsGranted)
                    permissionsState.launchMultiplePermissionRequest()

            }


            PermissionsRequired(
                multiplePermissionsState = permissionsState,
                permissionsNotGrantedContent = {
                    "permissionNotGrantedContent".log()
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                            Text(stringResource(R.string.request_permissions))
                        }
                    }
                },
                permissionsNotAvailableContent = {
                    "permissionNotAvailableContent".log()
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.camera_permission_denied),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                content = {
                    // SurfaceView
                    AndroidView(
                        factory = {
                            SurfaceView(it).apply {
//                            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                                roomViewModel.setupLocalPreview(this)
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                })

            Column(
                Modifier.systemBarsPadding(
                    additionalStart = 12.dp,
                    additionalEnd = 12.dp
                ).fillMaxSize(),
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
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.nav_back),
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
                            stringResource(R.string.title_studio_name),
                            style = MaterialTheme.typography.body2
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = pendingRoomInfo.name,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            if (!animateStart) {
                                roomViewModel.randomRoomName()
                                animateStart = true
                            }
                        }) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.desc_random_studio_name),
                                modifier = Modifier.rotate(animateRotateDegree)
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // 底部"开始直播"按钮
                Button(
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    onClick = {
                        if (permissionsState.allPermissionsGranted)
                            roomViewModel.createRoom()
                        else Toast.makeText(
                            currentContext,
                            currentContext.getString(R.string.permissions_denied_in_need_of_live),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(text = stringResource(R.string.start_live))
                }
            }
        }
    }
}