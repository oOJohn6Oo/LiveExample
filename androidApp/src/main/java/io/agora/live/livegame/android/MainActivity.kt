package io.agora.live.livegame.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.agora.live.livegame.android.theme.LiveGameTheme
import io.agora.live.livegame.android.ui.create.CreateScreen
import io.agora.live.livegame.android.ui.HomeScreen
import io.agora.live.livegame.android.ui.RoomListScreen
import io.agora.live.livegame.android.ui.RoomListViewModel
import io.agora.live.livegame.android.ui.studio.StudioScreen
import io.agora.live.livegame.android.ui.studio.StudioViewModel
import io.agora.live.livegame.bean.RoomInfo
import io.agora.live.livegame.log
import java.net.URLEncoder

object RallyPage {
    const val Home = "home"
    const val RoomList = "roomList"
    const val Create = "create"
    const val Studio = "studio"
}

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { LiveGameApp() }
    }
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun LiveGameApp() {
    LiveGameTheme {
        val navController = rememberNavController()
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            RallyNavHost(navController)
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun RallyNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RallyPage.Home,
    ) {

        composable(RallyPage.Home) {
            HomeScreen {
                navController.navigate("${RallyPage.RoomList}/$it")
            }
        }

        composable(
            "${RallyPage.RoomList}/{sceneIndex}",
            arguments = listOf(navArgument("sceneIndex") {
                type = NavType.IntType
            })
        ) {
            val sceneIndex = it.arguments?.getInt("sceneIndex") ?: 0
            val pageName = stringArrayResource(R.array.scenes_name)[sceneIndex]

            RoomListScreen(pageName, nav2Room = { desiredRoom ->
                val desiredRoomInfo = RoomListViewModel.moshi.adapter(
                    RoomInfo::class.java
                ).toJson(desiredRoom)

                navController.navigate(
                    "${RallyPage.Studio}/${URLEncoder.encode(desiredRoomInfo, "UTF-8")}"
                )
            }) {
                navController.navigate(RallyPage.Create)
            }
        }

        composable(RallyPage.Create) {
            it.arguments?.putStringArray(
                "studioNameList",
                stringArrayResource(R.array.studio_name_list)
            )
            CreateScreen(popBack = { navController.popBackStack() }) { createdRoom ->
                createdRoom.toString().log()
                val desiredRoomJson = RoomListViewModel.moshi.adapter(RoomInfo::class.java)
                    .toJson(createdRoom)

                navController.navigate(
                    "${RallyPage.Studio}/${
                        URLEncoder.encode(
                            desiredRoomJson,
                            "UTF-8"
                        )
                    }"
                ) {
                    launchSingleTop = true
                    this.popUpTo(RallyPage.Create) {
                        inclusive = true
                    }
                }
            }
        }

        composable(
            route = "${RallyPage.Studio}/{${StudioViewModel.KEY_ROOM_INFO}}",
            arguments = listOf(navArgument(StudioViewModel.KEY_ROOM_INFO) {
                type = NavType.StringType
            })
        ) {
            StudioScreen(
                messages = listOf(
                    "AA: Aruba, Jamaga, wo I want to take ya",
                    "AA: Bermuda, Bahama, come on pretty mama",
                    "Kelago, Montego, wo I want to take you to the Kokomo",
                ), navBack = navController::popBackStack
            )
        }

    }
}