package com.aureon.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var liquidGlassEnabled by remember { mutableStateOf(true) }
    var lyricsAnimationEnabled by remember { mutableStateOf(true) }
    var equalizerEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Experimental", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }
            
            item {
                GlassSettingCard(
                    title = "Liquid Glass",
                    description = "iOS-style translucent materials across the app",
                    checked = liquidGlassEnabled,
                    onCheckedChange = { liquidGlassEnabled = it }
                )
            }
            
            item {
                GlassSettingCard(
                    title = "Lyrics Animation",
                    description = "Apple Fluid - Smooth spring scaling with dynamic focal tracking",
                    checked = lyricsAnimationEnabled,
                    onCheckedChange = { lyricsAnimationEnabled = it }
                )
            }
            
            item {
                GlassSettingCard(
                    title = "Equalizer",
                    description = "Enable audio equalizer",
                    checked = equalizerEnabled,
                    onCheckedChange = { equalizerEnabled = it }
                )
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text("Streaming Quality", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }
            
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        QualityOption("Max Quality", "Up to 24-bit / 192 kHz - Lossless Studio FLAC", true)
                        Spacer(Modifier.height(12.dp))
                        QualityOption("Hi-Res Audio", "24-bit / 96 kHz - Lossless Studio FLAC", false)
                        Spacer(Modifier.height(12.dp))
                        QualityOption("CD Lossless", "16-bit / 44.1kHz - Lossless CD FLAC", false)
                        Spacer(Modifier.height(12.dp))
                        QualityOption("Standard Quality", "320 kbps - MP3 (Data Saver)", false)
                    }
                }
            }
        }
    }
}

@Composable
fun GlassSettingCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
fun QualityOption(title: String, description: String, selected: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        if (selected) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.CheckCircle,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
