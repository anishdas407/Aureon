package com.aureon.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aureon.app.LyricLine

enum class LyricAnimationStyle {
    APPLE_FLUID,
    KARAOKE_PULSE,
    KINETIC_SLIDE,
    BOUNCE,
    WAVE
}

@Composable
fun KineticLyrics(
    lyrics: List<LyricLine>,
    currentPositionMs: Long,
    animationStyle: LyricAnimationStyle = LyricAnimationStyle.APPLE_FLUID,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val activeIndex = remember(currentPositionMs, lyrics) {
        lyrics.indexOfLast { it.timeMs <= currentPositionMs }
    }

    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            listState.animateScrollToItem(
                index = activeIndex,
                scrollOffset = -200
            )
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(lyrics) { index, line ->
            LyricLineItem(
                line = line,
                isActive = index == activeIndex,
                animationStyle = animationStyle
            )
        }
    }
}

@Composable
private fun LyricLineItem(
    line: LyricLine,
    isActive: Boolean,
    animationStyle: LyricAnimationStyle
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = when (animationStyle) {
            LyricAnimationStyle.APPLE_FLUID -> spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            LyricAnimationStyle.KARAOKE_PULSE -> spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium)
            LyricAnimationStyle.KINETIC_SLIDE -> tween(durationMillis = 400, easing = FastOutSlowInEasing)
            LyricAnimationStyle.BOUNCE -> spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessVeryLow)
            LyricAnimationStyle.WAVE -> spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
        }
    )

    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.4f,
        animationSpec = tween(300)
    )

    val yOffset by animateFloatAsState(
        targetValue = if (isActive) 0f else if (animationStyle == LyricAnimationStyle.KINETIC_SLIDE) 20f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Text(
        text = line.text,
        fontSize = if (isActive) 24.sp else 18.sp,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
        color = if (isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .scale(scale)
            .alpha(alpha)
            .graphicsLayer { translationY = yOffset }
    )
}
