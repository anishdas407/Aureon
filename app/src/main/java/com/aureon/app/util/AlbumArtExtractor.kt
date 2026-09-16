package com.aureon.app.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AlbumColors(
    val primary: Int,
    val secondary: Int,
    val background: Int
)

object AlbumArtExtractor {
    suspend fun extractColors(context: Context, albumArtUri: String?): AlbumColors? = withContext(Dispatchers.IO) {
        if (albumArtUri == null) return@withContext null

        try {
            val uri = Uri.parse(albumArtUri)
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val palette = Palette.from(bitmap).generate()
                val primary = palette.getVibrantColor(0xFF6200EE.toInt())
                val secondary = palette.getLightVibrantColor(0xFF03DAC5.toInt())
                val background = palette.getDarkMutedColor(0xFF121212.toInt())
                AlbumColors(primary, secondary, background)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
