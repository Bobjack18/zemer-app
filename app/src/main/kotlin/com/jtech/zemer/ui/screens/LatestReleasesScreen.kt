package com.jtech.zemer.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jtech.zemer.LocalDatabase
import com.jtech.zemer.LocalPlayerAwareWindowInsets
import com.jtech.zemer.LocalPlayerConnection
import com.jtech.zemer.R
import com.jtech.zemer.latestreleases.LatestReleaseCard
import com.jtech.zemer.latestreleases.isPlayableSingle
import com.jtech.zemer.latestreleases.shufflePlay
import com.jtech.zemer.ui.component.ChipsRow
import com.jtech.zemer.ui.component.HideOnScrollFAB
import com.jtech.zemer.ui.component.IconButton
import com.jtech.zemer.ui.component.LocalMenuState
import com.jtech.zemer.ui.menu.SelectionMediaMetadataMenu
import com.jtech.zemer.ui.utils.backToMain
import com.jtech.zemer.viewmodels.LatestReleasesViewModel
import kotlinx.coroutines.launch

/** The kind of release shown by the See-all filter chips: everything, multi-track albums, or singles. */
private enum class ReleaseFilter { ALL, ALBUMS, SONGS }

/**
 * The "See all" screen for the Home Latest Releases section: the full kosher latest-releases feed as
 * a vertical list, newest-first. Uses its own [LatestReleasesViewModel] instance but is backed by the
 * same process-wide [com.jtech.zemer.latestreleases.LatestReleasesStore] cache as the Home section, so
 * the feed is reused (a conditional refresh, not a fresh fetch). When the feed is empty/unavailable the
 * list is simply empty — nothing is forced.
 *
 * Adds filter chips (All / Albums / Songs, split on [isPlayableSingle]) and multi-select: long-press a
 * row to enter selection, then the selection top bar's ⋮ opens the shared [SelectionMediaMetadataMenu]
 * acting on each selected release's lead (sample) track. The shuffle FAB plays the (filtered) feed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestReleasesScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: LatestReleasesViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val menuState = LocalMenuState.current
    val database = LocalDatabase.current
    val coroutineScope = rememberCoroutineScope()
    val playerConnection = LocalPlayerConnection.current ?: return
    val isPlaying by playerConnection.isPlaying.collectAsState()
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
    val releases by viewModel.releases.collectAsState()
    val lazyListState = rememberLazyListState()

    var filter by rememberSaveable { mutableStateOf(ReleaseFilter.ALL) }
    val displayed = remember(releases, filter) {
        when (filter) {
            ReleaseFilter.ALL -> releases
            ReleaseFilter.ALBUMS -> releases.filterNot { it.isPlayableSingle() }
            ReleaseFilter.SONGS -> releases.filter { it.isPlayableSingle() }
        }
    }

    var inSelectionMode by rememberSaveable { mutableStateOf(false) }
    val selectedIds: SnapshotStateList<String> = rememberSaveable(
        saver = listSaver(save = { it.toList() }, restore = { it.toMutableStateList() }),
    ) { mutableStateListOf() }
    fun exitSelection() {
        inSelectionMode = false
        selectedIds.clear()
    }

    // Keep selection consistent with what's actually shown (the feed can refresh in the background):
    // drop ids no longer displayed, and leave selection mode if nothing remains.
    val displayedIds = remember(displayed) { displayed.mapTo(HashSet()) { it.browseId } }
    LaunchedEffect(displayedIds) {
        if (inSelectionMode) {
            selectedIds.retainAll(displayedIds)
            if (selectedIds.isEmpty()) inSelectionMode = false
        }
    }
    if (inSelectionMode) {
        BackHandler(onBack = ::exitSelection)
    }
    val allSelected = displayed.isNotEmpty() && displayedIds.all { it in selectedIds }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
        ) {
            if (!inSelectionMode && releases.isNotEmpty()) {
                item(key = "filter") {
                    ChipsRow(
                        chips = listOf(
                            ReleaseFilter.ALL to stringResource(R.string.filter_all),
                            ReleaseFilter.ALBUMS to stringResource(R.string.albums),
                            ReleaseFilter.SONGS to stringResource(R.string.songs),
                        ),
                        currentValue = filter,
                        onValueUpdate = {
                            filter = it
                            exitSelection()
                        },
                    )
                }
            }
            items(
                items = displayed,
                key = { it.browseId },
            ) { release ->
                LatestReleaseCard(
                    release = release,
                    navController = navController,
                    playerConnection = playerConnection,
                    database = database,
                    mediaMetadata = mediaMetadata,
                    isPlaying = isPlaying,
                    asGrid = false,
                    inSelectionMode = inSelectionMode,
                    isSelected = release.browseId in selectedIds,
                    onSelectedChange = { selected ->
                        if (selected) {
                            if (release.browseId !in selectedIds) selectedIds.add(release.browseId)
                            inSelectionMode = true
                        } else {
                            selectedIds.remove(release.browseId)
                            if (selectedIds.isEmpty()) inSelectionMode = false
                        }
                    },
                )
            }
        }

        HideOnScrollFAB(
            visible = displayed.isNotEmpty() && !inSelectionMode,
            lazyListState = lazyListState,
            icon = R.drawable.shuffle,
            onClick = { displayed.shufflePlay(playerConnection, context.getString(R.string.latest_releases)) },
        )
    }

    TopAppBar(
        title = {
            Text(
                if (inSelectionMode) {
                    pluralStringResource(R.plurals.n_release_selected, selectedIds.size, selectedIds.size)
                } else {
                    stringResource(R.string.latest_releases)
                },
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { if (inSelectionMode) exitSelection() else navController.navigateUp() },
                onLongClick = { if (!inSelectionMode) navController.backToMain() },
            ) {
                Icon(
                    painterResource(if (inSelectionMode) R.drawable.close else R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        },
        actions = {
            if (inSelectionMode) {
                IconButton(
                    onClick = {
                        if (allSelected) {
                            exitSelection()
                        } else {
                            selectedIds.clear()
                            selectedIds.addAll(displayedIds)
                        }
                    },
                    onLongClick = {},
                ) {
                    Icon(
                        painterResource(if (allSelected) R.drawable.deselect else R.drawable.select_all),
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = {
                        val chosen = displayed.filter { it.browseId in selectedIds }
                        coroutineScope.launch {
                            // Resolve to the actual songs (album -> tracklist, single -> its track) in
                            // the ViewModel, then act via the shared selection menu. No DB/network in UI.
                            val songs = viewModel.resolveSelectionToSongs(chosen)
                            if (songs.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.latest_releases_load_failed),
                                    Toast.LENGTH_SHORT,
                                ).show()
                                return@launch
                            }
                            menuState.show {
                                SelectionMediaMetadataMenu(
                                    songSelection = songs,
                                    currentItems = emptyList(),
                                    onDismiss = menuState::dismiss,
                                    clearAction = { exitSelection() },
                                )
                            }
                        }
                    },
                    onLongClick = {},
                ) {
                    Icon(painterResource(R.drawable.more_vert), contentDescription = null)
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
