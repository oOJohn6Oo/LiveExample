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
import io.agora.live.livegame.android.ui.CreateScreen
import io.agora.live.livegame.android.ui.HomeScreen
import io.agora.live.livegame.android.ui.RoomListScreen
import io.agora.live.livegame.android.ui.StudioScreen
import io.agora.live.livegame.log

object RallyPage{
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
            HomeScreen{
                navController.navigate("${RallyPage.RoomList}/$it")
            }
        }

        composable("${RallyPage.RoomList}/{sceneIndex}", arguments = listOf(navArgument("sceneIndex"){
            type = NavType.IntType
        })) {
            val sceneIndex = it.arguments?.getInt("sceneIndex")?:0
            val pageName = stringArrayResource(R.array.scenes_name)[sceneIndex]

            RoomListScreen(pageName, nav2Room = {navController.navigate(RallyPage.Studio)}){
                navController.navigate(RallyPage.Create)
            }
        }
        composable(RallyPage.Create) {
            it.arguments?.putStringArray("studioNameList", stringArrayResource(R.array.studio_name_list))
            CreateScreen(popBack = {navController.popBackStack()}){ createdRoom ->
                createdRoom.toString().log()
                navController.navigate(RallyPage.Studio){
                    launchSingleTop = true
                    this.popUpTo(RallyPage.Create){
                        inclusive = true
                    }
                }
            }
        }
        composable(RallyPage.Studio) {
            StudioScreen(listOf(
                "AA: Aruba, Jamaga, wo I want to take ya",
                "AA: Bermuda, Bahama, come on pretty mama",
                "Kelago, Montego, wo I want to take you to the Kokomo",
            ))
        }

    }
}