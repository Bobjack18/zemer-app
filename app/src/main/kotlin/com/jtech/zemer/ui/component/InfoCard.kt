package com.jtech.zemer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jtech.zemer.ui.theme.Dimens

/**
 * Standard rich-status block: a tonal rounded card with an optional icon + title + supporting text,
 * plus optional [content] and an [action] row. Use this instead of a bespoke `Card` for status
 * surfaces (see docs/ui/standards.md).
 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    title: String? = null,
    text: String? = null,
    action: (@Composable () -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.ScreenPaddingH, vertical = Dimens.space2),
    ) {
        Column(Modifier.padding(Dimens.space4)) {
            if (icon != null || title != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        icon()
                        Spacer(Modifier.width(Dimens.space3))
                    }
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            if (text != null) {
                if (icon != null || title != null) Spacer(Modifier.height(Dimens.space2))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            content?.let {
                Spacer(Modifier.height(Dimens.space3))
                it()
            }
            action?.let {
                Spacer(Modifier.height(Dimens.space3))
                it()
            }
        }
    }
}

/**
 * Compact inline status: an optional leading icon + text in one row (e.g. "Signed in"). For richer
 * blocks use [InfoCard].
 */
@Composable
fun StatusRow(
    text: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = Dimens.ScreenPaddingH, vertical = Dimens.space2),
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.width(Dimens.space3))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
