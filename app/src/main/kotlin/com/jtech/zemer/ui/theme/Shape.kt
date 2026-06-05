package com.jtech.zemer.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * App shape scale (standard Material 3). Wired into the theme as `MaterialTheme.shapes`; use
 * `MaterialTheme.shapes.*` and [PillShape] instead of `RoundedCornerShape(N.dp)` literals in screens
 * (see docs/ui/standards.md, R9).
 *
 *   extraSmall  8dp  - inline chips, small thumbnails
 *   small      12dp  - rows, list items
 *   medium     16dp  - cards, surfaces
 *   large      20dp  - prominent cards, sheets
 *   extraLarge 28dp  - dialogs, bottom sheets
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Fully rounded (pill) - chips, toggles, FABs. Not part of the M3 shapes scale. */
val PillShape = RoundedCornerShape(percent = 50)
