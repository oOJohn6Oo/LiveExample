package io.agora.live.livegame.android.ui

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import io.agora.live.livegame.android.R
import io.agora.live.livegame.android.util.systemBarsPadding
import io.agora.live.livegame.bean.RoomInfo
import kotlin.random.Random

@Preview
@Composable
fun PreviewCreateScreen() {
    CreateScreen({}) {

    }
}

@Composable
fun CreateScreen(popBack: () -> Unit, nav2Studio: (createdRoom: RoomInfo) -> Unit) {
    ProvideWindowInsets {

        Box {

//        AndroidView()

            Column(
                Modifier.systemBarsPadding(
                    additionalStart = 12.dp,
                    additionalEnd = 12.dp
                ).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val studioNameList = stringArrayResource(R.array.studio_name_list)

                var currentName by remember { mutableStateOf(studioNameList[Random.nextInt(Int.MAX_VALUE) % studioNameList.size]) }

                var animateStart by remember { mutableStateOf(false) }

                val animateRotateDegree by animateFloatAsState(
                    if (animateStart) 360f else 0f,
                    TweenSpec(if (animateStart) 500 else 0),
                    finishedListener = {
                        if (animateStart) animateStart = false
                    }
                )

                IconButton(onClick = popBack, modifier = Modifier.align(Alignment.Start)) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = stringResource(R.string.nav_back))
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
                            text = currentName,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            animateStart = true
                            currentName =
                                studioNameList[Random.nextInt(Int.MAX_VALUE) % studioNameList.size]
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

                Button(
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    onClick = {nav2Studio(RoomInfo("","",""))},
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(text = stringResource(R.string.start_live))
                }
            }
        }
    }
}