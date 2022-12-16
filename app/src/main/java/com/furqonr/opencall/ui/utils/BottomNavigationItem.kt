package com.furqonr.opencall.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.furqonr.opencall.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Phone
import compose.icons.fontawesomeicons.solid.User

sealed class BottomNavigationItem(
    val route: String,
    @DrawableRes val filledIcon: Int,
    @DrawableRes val icon: Int,
    val label: String
) {
    object Dashboard :
        BottomNavigationItem(
            route = "dashboard",
            label = "Dashboard",
            filledIcon = R.drawable.ic_baseline_chat_24,
            icon = R.drawable.ic_outline_chat_24
        )

    object Profile :
        BottomNavigationItem(
            route = "profile",
            label = "Profile",
            filledIcon = R.drawable.ic_baseline_person_24,
            icon = R.drawable.ic_outline_person_24
        )

    object Settings :
        BottomNavigationItem(
            route = "settings",
            label = "Settings",
            filledIcon = R.drawable.ic_baseline_settings_24,
            icon = R.drawable.ic_outline_settings_24
        )

    object Call : BottomNavigationItem(
        route = "call",
        label = "Call",
        filledIcon = R.drawable.ic_baseline_call_24,
        icon = R.drawable.ic_outline_call_24
    )
}
