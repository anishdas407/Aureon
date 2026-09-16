package com.aureon.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aureon.app.ui.theme.AureonTheme
import com.aureon.app.ui.HomeScreen
import com.aureon.app.ui.PlaylistsScreen
import com.aureon.app.ui.SettingsScreen
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestAudioPermission()

        setContent {
            AureonTheme {
                val viewModel: PlayerViewModel = viewModel()
                val navController = rememberNavController()
                
                val tracks by viewModel.tracks.collectAsState()
                
                // Request permission and scan on launch
                LaunchedEffect(Unit) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                Manifest.permission.READ_MEDIA_AUDIO
                            else
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Already have permission, scan will happen in ViewModel
                    }
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                            
                            NavigationBarItem(
                                selected = currentRoute == Screen.Home.route,
                                onClick = { navController.navigate(Screen.Home.route) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Screen.Playlists.route,
                                onClick = { navController.navigate(Screen.Playlists.route) },
                                icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Playlists") },
                                label = { Text("Playlists") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Screen.Settings.route,
                                onClick = { navController.navigate(Screen.Settings.route) },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onTrackClick = { track ->
                                    viewModel.play(track)
                                    navController.navigate(Screen.NowPlaying.createRoute(track.id))
                                }
                            )
                        }
                        composable(Screen.Playlists.route) {
                            PlaylistsScreen(
                                viewModel = viewModel,
                                onTrackClick = { track ->
                                    viewModel.play(track)
                                    navController.navigate(Screen.NowPlaying.createRoute(track.id))
                                }
                            )
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen()
                        }
                        composable("now_playing/{trackId}") { backStackEntry ->
                            val trackId = backStackEntry.arguments?.getString("trackId")?.toLongOrNull()
                            NowPlayingScreen(
                                viewModel = viewModel,
                                trackId = trackId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestAudioPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permission)
        }
    }
}
