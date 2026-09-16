package com.aureon.app

import java.io.File

data class LyricLine(val timeMs: Long, val text: String)

object LrcParser {
    fun parse(path: String?): List<LyricLine> {
        if (path == null) return emptyList()
        val file = File(path)
        if (!file.exists()) return emptyList()

        val lines = mutableListOf<LyricLine>()
        val regex = Regex("""\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)""")

        file.forEachLine { line ->
            regex.find(line)?.let { match ->
                val minutes = match.groupValues[1].toLong()
                val seconds = match.groupValues[2].toLong()
                val milliseconds = match.groupValues[3].padEnd(3, '0').toLong()
                val text = match.groupValues[4].trim()
                val totalMs = (minutes * 60 * 1000) + (seconds * 1000) + milliseconds
                if (text.isNotBlank()) {
                    lines.add(LyricLine(totalMs, text))
                }
            }
        }
        return lines.sortedBy { it.timeMs }
    }
}
