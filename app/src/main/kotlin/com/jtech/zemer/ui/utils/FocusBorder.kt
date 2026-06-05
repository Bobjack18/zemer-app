package com.jtech.zemer.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The shared D-pad focus affordance: makes the element `focusable()` and draws an animated
 * [color] border (fading in/out of transparent) while it holds focus. Replaces the hand-rolled
 * `remember mutableStateOf` + `animateColorAsState` + `border` + `onFocusChanged` idiom that was
 * copy-pasted across the player and other custom focusable controls.
 */
fun Modifier.dpadFocusBorder(
    color: Color,
    shape: Shape,
    width: Dp = 3.dp,
): Modifier = composed {
    var focused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (focused) color else Color.Transparent,
        label = "dpad_focus_border",
    )
    border(width, borderColor, shape)
        .onFocusChanged { focused = it.isFocused }
        .focusable()
}
