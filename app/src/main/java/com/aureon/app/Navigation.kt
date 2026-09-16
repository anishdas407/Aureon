package com.aureon.app

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Playlists : Screen("playlists")
    object Settings : Screen("settings")
    object NowPlaying : Screen("now_playing/{trackId}") {
        fun createRoute(trackId: Long) = "now_playing/$trackId"
    }
}
