package io.agora.live.livegame.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.agora.live.livegame.Greeting
import io.agora.live.livegame.LiveChannel
import io.agora.live.livegame.android.ui.CreateScreen
import io.agora.live.livegame.android.ui.MainScreen
import io.agora.live.livegame.android.ui.RoomListScreen
import io.agora.live.livegame.android.ui.StudioScreen
import io.agora.live.livegame.android.ui.theme.LiveGameTheme
import io.agora.live.livegame.bean.RoomInfo

fun greet(): String {
    return Greeting().greeting()
}

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    val mainViewModel :MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { LiveGameApp() }
    }
}

@ExperimentalFoundationApi
@Composable
fun LiveGameApp() {
    LiveGameTheme {
        val navController = rememberNavController()
//        val backstackEntry = navController.currentBackStackEntryAsState()
//        val currentScreen = NGApiScreen.fromRoute(backstackEntry.value?.destination?.route)


//        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
//            if()
//        })

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            RallyNavHost(navController)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun RallyNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            (LocalContext.current as MainActivity).mainViewModel.destroySDK()
            MainScreen{
                navController.navigate("roomList")
            }
        }
//        composable("${DescScreen.name}/{${PageDesc.key}}"
//            ,arguments = listOf(navArgument(PageDesc.key){
//                type = NavType.StringType
//            })) { entry->
//            val param = entry.arguments?.getString(PageDesc.key)
//            val screen = NGApiScreen.fromRoute(param)
//            DescBody(screen.pageDesc)
//        }
        composable("roomList") {
            (LocalContext.current as MainActivity).mainViewModel.initSDK(LiveChannel.PK)
            val roomList = listOf(
                RoomInfo("id1", "name1","owner1"),
                RoomInfo("id2", "name2","owner2"),
                RoomInfo("id3", "name3","owner3"),
                RoomInfo("id4", "name4","owner4"),
                RoomInfo("id5", "name5","owner5"),
                RoomInfo("id6", "name6","owner6"),
                RoomInfo("id7", "name7","owner7"),
            )
            RoomListScreen(roomList, nav2Room = {navController.navigate("studio")}){
                navController.navigate("create")
            }
        }
        composable("create") {
            CreateScreen(popBack = {navController.popBackStack()}){ createdRoom ->
                navController.popBackStack()
                navController.navigate("studio")
            }
        }
        composable("studio") {
            StudioScreen(listOf(
                "AA: Aruba, Jamaga, wo I want to take ya",
                "AA: Bermuda, Bahama, come on pretty mama",
                "Kelago, Montego, wo I want to take you to the Kokomo",
            ))
        }

//        val accountsName = Accounts.name
//        composable(
//            route = "$accountsName/{name}",
//            arguments = listOf(
//                navArgument("name") {
//                    type = NavType.StringType
//                }
//            ),
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "rally://$accountsName/{name}"
//                }
//            ),
//        ) { entry ->
//            val accountName = entry.arguments?.getString("name")
//            val account = UserData.getAccount(accountName)
//            SingleAccountBody(account = account)
//        }
    }
}