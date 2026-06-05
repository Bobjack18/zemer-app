package com.jtech.zemer.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing and sizing tokens. The one source of truth for dp values in the UI - use these instead of
 * bare `.dp` literals (see docs/ui/standards.md, R9).
 *
 * Scale is 4dp-based. Prefer the semantic aliases where one fits.
 */
object Dimens {
    // 4dp base scale
    val space1 = 4.dp
    val space2 = 8.dp
    val space3 = 12.dp
    val space4 = 16.dp
    val space6 = 24.dp
    val space8 = 32.dp

    // Semantic aliases
    val ScreenPaddingH = 16.dp   // horizontal page padding
    val ItemGap = 8.dp           // gap between sibling controls
    val IconSize = 24.dp         // standard icon
    val MinTouchTarget = 48.dp   // minimum interactive size (R12)
    val DialogPadding = 24.dp    // dialog content padding (R7)
}
