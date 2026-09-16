package com.aureon.app

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val uri: String,
    val durationMs: Long,
    val lrcPath: String?,
    val albumArtUri: String? = null
)
