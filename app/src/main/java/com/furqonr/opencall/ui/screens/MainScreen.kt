package com.furqonr.opencall.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.furqonr.opencall.MainActivity
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.ui.screens.call.Call
import com.furqonr.opencall.ui.screens.dashboard.Dashboard
import com.furqonr.opencall.ui.screens.settings.Settings
import com.furqonr.opencall.ui.theme.Green700
import com.furqonr.opencall.ui.utils.BottomNavigationItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userState: (Boolean) -> Unit = {},
    navController: NavHostController
) {
    val context = (LocalContext.current as MainActivity)
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    val viewModel: MainViewModel = viewModel()

    val (indexScreen, setIndexScreen) = remember { mutableStateOf(0) }


    val (currentUser) = viewModel.currentUser

    val items = listOf(
        BottomNavigationItem.Dashboard,
        BottomNavigationItem.Call,
        BottomNavigationItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                contentColor = Green700,
            ) {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (index == indexScreen) screen.filledIcon else screen.icon),
                                contentDescription = screen.label,
                            )
                        },
                        label = {
                            Text(text = screen.label)
                        },
                        alwaysShowLabel = true,
                        selected = indexScreen == index,
                        onClick = {
                            if (indexScreen != index) {
                                setIndexScreen(index)
                            }
                        }
                    )
                }
            }
        }
    ) {
        when (indexScreen) {
            0 -> Wrapper(modifier = Modifier.padding(it)) {
                Dashboard(navController = navController)
            }
            1 -> Wrapper(modifier = Modifier.padding(it)) {
                Call()
            }
            2 -> Wrapper(modifier = Modifier.padding(it)) {
                Settings()
            }
        }
    }

    LaunchedEffect(key1 = currentUser) {
        userState(currentUser)
    }

    BackHandler {
        if (indexScreen == 0) {
            context.finish()
        } else {
            setIndexScreen(0)
        }
    }

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
    }

}

@Composable
fun Wrapper(modifier: Modifier = Modifier, content: @Composable () -> Unit) {

    Column(modifier = modifier) {
        content()
    }

}