package com.jtech.zemer.ui.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * Shared animation specs so motion is consistent across the app (see docs/ui/standards.md, R9). Use
 * these for transitions, expand/collapse, selection, and visibility changes - no ad-hoc durations.
 */
object Motion {
    const val ShortMs = 150
    const val MediumMs = 250
    const val LongMs = 400

    /** Springy emphasis for selection / primary actions / enter transitions. */
    fun <T> emphasizedSpring(): FiniteAnimationSpec<T> =
        spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)

    /** Calm, non-bouncy motion for layout changes. */
    fun <T> standardSpring(): FiniteAnimationSpec<T> =
        spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)

    fun <T> short(): FiniteAnimationSpec<T> = tween(durationMillis = ShortMs)
    fun <T> medium(): FiniteAnimationSpec<T> = tween(durationMillis = MediumMs)
    fun <T> long(): FiniteAnimationSpec<T> = tween(durationMillis = LongMs)
}
