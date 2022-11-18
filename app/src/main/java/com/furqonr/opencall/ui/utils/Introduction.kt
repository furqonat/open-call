package com.furqonr.opencall.ui.utils

import androidx.navigation.NamedNavArgument

object Introduction {
    val intro = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "intro"
    }
    val detail = object : NavigationCommand {
        override val arguments: List<NamedNavArgument> = emptyList()
        override val destination: String = "detail"
    }
}

interface NavigationCommand {
    val arguments: List<NamedNavArgument>
    val destination: String
}
