package com.aureon.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aureon.app.ui.LibraryScreen
import com.aureon.app.ui.NowPlayingScreen
import com.aureon.app.ui.theme.AureonTheme

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
                var currentTrack by remember { mutableStateOf<Track?>(null) }

                if (currentTrack == null) {
                    LibraryScreen(
                        viewModel = viewModel,
                        onTrackClick = { track ->
                            viewModel.play(track)
                            currentTrack = track
                        }
                    )
                } else {
                    NowPlayingScreen(
                        viewModel = viewModel,
                        onBack = { currentTrack = null }
                    )
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
