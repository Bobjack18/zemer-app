package com.jtech.zemer.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jtech.zemer.constants.ListItemHeight
import com.jtech.zemer.constants.ListThumbnailSize

/** One artist entry for [SelectArtistDialog]; entries without an [id] are not selectable and skipped. */
data class SelectableArtist(
    val id: String?,
    val name: String,
    val thumbnailUrl: String? = null,
)

/**
 * The shared "which artist?" picker shown when an item credits several artists. Replaces the five
 * hand-rolled `ListDialog` copies that lived in the menus. Rows are D-pad focusable with the
 * standard `surfaceVariant` focus background; [onSelect] receives the chosen artist id (the caller
 * navigates and closes whatever it owns).
 */
@Composable
fun SelectArtistDialog(
    artists: List<SelectableArtist>,
    onDismiss: () -> Unit,
    onSelect: (artistId: String) -> Unit,
) {
    ListDialog(
        onDismiss = onDismiss,
    ) {
        items(
            items = artists.filter { it.id != null }.distinctBy { it.id },
            key = { it.id!! },
        ) { artist ->
            var isFocused by remember { mutableStateOf(false) }
            val backgroundColor by animateColorAsState(
                targetValue = if (isFocused) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                label = "artist_dialog_focus_bg",
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ListItemHeight)
                    .onFocusChanged { isFocused = it.isFocused }
                    .clickable { onSelect(artist.id!!) }
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp),
            ) {
                if (artist.thumbnailUrl != null) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            model = artist.thumbnailUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(ListThumbnailSize)
                                .clip(CircleShape),
                        )
                    }
                }
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                )
            }
        }
    }
}
