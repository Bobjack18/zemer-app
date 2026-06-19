package com.jtech.zemer.latestreleases

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.jtech.zemer.LocalDatabase
import com.jtech.zemer.R
import com.jtech.zemer.db.MusicDatabase
import com.jtech.zemer.models.MediaMetadata
import com.jtech.zemer.playback.PlayerConnection
import com.jtech.zemer.ui.component.AlbumBadges
import com.jtech.zemer.ui.component.IconButton
import com.jtech.zemer.ui.component.LocalMenuState
import com.jtech.zemer.ui.component.SongBadges
import com.jtech.zemer.ui.component.YouTubeGridItem
import com.jtech.zemer.ui.component.YouTubeListItem
import com.jtech.zemer.ui.menu.SongMenu
import com.jtech.zemer.ui.menu.YouTubeAlbumMenu
import com.jtech.zemer.utils.joinByBullet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * One Latest Releases card, shared by the Home shelf ([asGrid] = true, a [YouTubeGridItem]) and the
 * "See all" list ([asGrid] = false, a [YouTubeListItem]). Keeping the binding here means it exists
 * once and can't drift between the two surfaces.
 *
 * Everything is at the release's natural granularity: a **single** (1-track release) behaves as its
 * song — the row shows the song's library badges ([SongBadges]) and the per-item menu is [SongMenu];
 * an **album** behaves as the album — album badges ([AlbumBadges]) and [YouTubeAlbumMenu]. So
 * liking/downloading from a menu lights the matching badge on the row (no lead-track proxy).
 *
 * Selection (See-all list only): pass [onSelectedChange] to enable multi-select. Long-press then
 * toggles selection (entering selection mode), tap toggles while [inSelectionMode], and the per-item
 * menu moves to a trailing ⋮ (long-press is taken by selection). The Home shelf passes no
 * [onSelectedChange], so its long-press keeps opening the per-item menu.
 *
 * [coroutineScope] is only used by the grid variant's album play button; the list variant ignores it.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LatestReleaseCard(
    release: LatestRelease,
    navController: NavController,
    playerConnection: PlayerConnection,
    database: MusicDatabase,
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    asGrid: Boolean,
    coroutineScope: CoroutineScope? = null,
    inSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onSelectedChange: ((Boolean) -> Unit)? = null,
) {
    val menuState = LocalMenuState.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val album = remember(release.browseId) { release.toAlbumItem() }
    val dateLabel = remember(release.browseId) { release.relativeDateLabel() }
    val subtitle = joinByBullet(release.artistName, dateLabel)
    val selectable = onSelectedChange != null

    fun showItemMenu() {
        if (release.isPlayableSingle()) {
            // A single is a song: show the song menu, backed by the canonical DB row (insert is
            // IGNORE-safe, so an already-known song keeps its real fields).
            scope.launch {
                val song = withContext(Dispatchers.IO) {
                    release.sampleMediaMetadata()?.let { meta ->
                        database.insert(meta)
                        database.song(meta.id).first()
                    }
                }
                menuState.show {
                    if (song != null) {
                        SongMenu(originalSong = song, navController = navController, onDismiss = menuState::dismiss)
                    } else {
                        YouTubeAlbumMenu(albumItem = album, navController = navController, onDismiss = menuState::dismiss)
                    }
                }
            }
        } else {
            menuState.show {
                YouTubeAlbumMenu(albumItem = album, navController = navController, onDismiss = menuState::dismiss)
            }
        }
    }

    val clickable = Modifier.combinedClickable(
        onClick = {
            if (inSelectionMode) onSelectedChange?.invoke(!isSelected)
            else release.openOrPlay(navController, playerConnection, database)
        },
        onLongClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            if (selectable) onSelectedChange?.invoke(!isSelected) else showItemMenu()
        },
    )

    if (asGrid) {
        YouTubeGridItem(
            item = album,
            subtitleOverride = subtitle,
            centeredPlayButton = release.isPlayableSingle(),
            isActive = release.isNowPlaying(mediaMetadata),
            isPlaying = isPlaying,
            coroutineScope = coroutineScope,
            thumbnailRatio = 1f,
            badges = { ReleaseBadges(release) },
            modifier = clickable,
        )
    } else {
        YouTubeListItem(
            item = album,
            subtitleOverride = subtitle,
            centeredPlayButton = release.isPlayableSingle() && !inSelectionMode,
            isActive = release.isNowPlaying(mediaMetadata),
            isPlaying = isPlaying,
            isSelected = isSelected && inSelectionMode,
            badges = { ReleaseBadges(release) },
            trailingContent = {
                if (!inSelectionMode && selectable) {
                    IconButton(onClick = { showItemMenu() }, onLongClick = { showItemMenu() }) {
                        Icon(painterResource(R.drawable.more_vert), contentDescription = null)
                    }
                }
            },
            modifier = clickable,
        )
    }
}

/**
 * The release's library badges at its natural granularity: a single shows its song's
 * liked/in-library/download state; an album shows its bookmark + aggregate download state. Reads only
 * the DB, so a release the user has never touched shows nothing (correct), and anything done via a
 * menu lights up here reactively.
 */
@Composable
private fun RowScope.ReleaseBadges(release: LatestRelease) {
    val database = LocalDatabase.current
    if (release.isPlayableSingle()) {
        val songId = release.sampleVideoId
        if (!songId.isNullOrEmpty()) {
            val song by remember(songId) { database.song(songId) }.collectAsState(initial = null)
            song?.let { SongBadges(song = it, showInLibraryIcon = true) }
        }
    } else {
        val albumEntity by remember(release.browseId) { database.album(release.browseId) }
            .collectAsState(initial = null)
        albumEntity?.let { AlbumBadges(album = it) }
    }
}
