package com.jtech.zemer.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.dp

/** Seek step per D-pad left/right press, as a fraction of the track. */
private const val SeekKeyStep = 0.02f

@Composable
fun BigSeekBar(
    progressProvider: () -> Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.13f),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    var width by remember {
        mutableFloatStateOf(0f)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Canvas(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .then(if (focused) Modifier.border(2.dp, color, MaterialTheme.shapes.medium) else Modifier)
            .onPlaced {
                width = it.size.width.toFloat()
            }
            .pointerInput(progressProvider) {
                detectHorizontalDragGestures { _, dragAmount ->
                    onProgressChange(
                        (progressProvider() + dragAmount * 1.2f / width).coerceIn(0f, 1f),
                    )
                }
            }
            // D-pad: focus the bar and seek with left/right so it is operable without touch (R10/R11).
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionRight -> {
                            onProgressChange((progressProvider() + SeekKeyStep).coerceIn(0f, 1f)); true
                        }
                        Key.DirectionLeft -> {
                            onProgressChange((progressProvider() - SeekKeyStep).coerceIn(0f, 1f)); true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .focusable(interactionSource = interactionSource),
    ) {
        drawRect(color = background)

        drawRect(
            color = color,
            size = size.copy(width = size.width * progressProvider()),
        )
    }
}
