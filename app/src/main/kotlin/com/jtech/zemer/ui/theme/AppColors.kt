package com.jtech.zemer.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * App-specific semantic colors for the few cases that legitimately fall outside the M3
 * `colorScheme` - text/icons/scrims drawn over media (album art, video, blurred backgrounds), where
 * white-on-media / black-scrim is conventional. Everything else must use `MaterialTheme.colorScheme`
 * (see docs/ui/standards.md, R9). AMOLED/pure-black is handled by `ColorScheme.pureBlack()`.
 */
object AppColors {
    /** Standard scrim over media to lift foreground content. */
    val scrim = Color.Black.copy(alpha = 0.4f)

    /** Foreground (text/icons) drawn directly over media. */
    val onMedia = Color.White

    /** Tunable black overlay over media (thumbnails, video, gradients). */
    fun mediaOverlay(alpha: Float) = Color.Black.copy(alpha = alpha)

    /** Tunable white foreground over media (e.g. faded secondary text). */
    fun onMedia(alpha: Float) = Color.White.copy(alpha = alpha)
}
