package com.jtech.zemer.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jtech.zemer.db.MusicDatabase
import com.jtech.zemer.latestreleases.LatestRelease
import com.jtech.zemer.latestreleases.LatestReleasesStore
import com.jtech.zemer.latestreleases.isPlayableSingle
import com.jtech.zemer.latestreleases.sampleMediaMetadata
import com.jtech.zemer.latestreleases.toAlbumItem
import com.jtech.zemer.models.MediaMetadata
import com.jtech.zemer.models.toMediaMetadata
import com.jtech.zemer.utils.filterWhitelisted
import com.metrolist.innertube.YouTube
import com.metrolist.innertube.models.AlbumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Backs the "Latest Releases" Home section and its full screen, deliberately separate from
 * [HomeViewModel] so a failure fetching the external feed can never affect the rest of Home.
 *
 * On creation it shows the disk-cached releases immediately (if any) and then refreshes once; both
 * passes run the releases through the app's existing [filterWhitelisted] so the user's content
 * preferences (female / KidZone / Israeli, by artist id) apply exactly as everywhere else. The list
 * is newest-first, as the server produced it.
 */
@HiltViewModel
class LatestReleasesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val database: MusicDatabase,
) : ViewModel() {
    private val _releases = MutableStateFlow<List<LatestRelease>>(emptyList())
    val releases: StateFlow<List<LatestRelease>> = _releases.asStateFlow()

    init {
        LatestReleasesStore.initialize(context)
        viewModelScope.launch(Dispatchers.IO) {
            val cached = LatestReleasesStore.cachedReleases()
            if (cached.isNotEmpty()) {
                val shown = filterReleases(cached)
                _releases.value = shown
                Timber.tag(TAG).d("Showing ${shown.size} cached releases (of ${cached.size} before whitelist filter)")
            }
            val fresh = LatestReleasesStore.refresh()
            val shown = filterReleases(fresh)
            _releases.value = shown
            Timber.tag(TAG).d("After refresh: ${shown.size} releases shown (of ${fresh.size} before whitelist filter)")
        }
    }

    /**
     * Keeps only releases whose artist passes the whitelist filter, preserving the feed order.
     * De-duplicates by [LatestRelease.browseId] first: the feed is external and may list one album
     * under more than one whitelisted artist, and browseId is the list key on both surfaces — a
     * duplicate would otherwise crash the Compose lists with a "key already used" error.
     */
    private suspend fun filterReleases(releases: List<LatestRelease>): List<LatestRelease> {
        if (releases.isEmpty()) return emptyList()
        val unique = releases.distinctBy { it.browseId }
        val allowedBrowseIds = unique.map { it.toAlbumItem() }
            .filterWhitelisted(database)
            .mapNotNull { (it as? AlbumItem)?.browseId }
            .toSet()
        return unique.filter { it.browseId in allowedBrowseIds }
    }

    /**
     * Resolves a multi-selection of releases to the actual songs to act on, at natural granularity:
     * a single becomes its one track; an album becomes its full tracklist (from the DB, fetched once
     * and cached if not present). De-duplicated by track id. Runs on IO; an album whose tracklist
     * can't be fetched contributes nothing rather than failing the whole action. This keeps all the
     * DB/network work out of the UI — the screen just feeds the result to the shared selection menu.
     */
    suspend fun resolveSelectionToSongs(releases: List<LatestRelease>): List<MediaMetadata> =
        withContext(Dispatchers.IO) {
            // Resolve releases concurrently — albums may each need a one-off network fetch, so doing
            // them in parallel keeps the menu from stalling on a large selection.
            coroutineScope {
                releases.map { release -> async { resolveReleaseToSongs(release) } }.awaitAll()
            }.flatten().distinctBy { it.id }
        }

    private suspend fun resolveReleaseToSongs(release: LatestRelease): List<MediaMetadata> {
        if (release.isPlayableSingle()) {
            val meta = release.sampleMediaMetadata() ?: return emptyList()
            database.insert(meta)
            return listOf(database.song(meta.id).first()?.toMediaMetadata() ?: meta)
        }
        var albumWithSongs = database.albumWithSongs(release.browseId).first()
        if (albumWithSongs?.songs.isNullOrEmpty()) {
            YouTube.album(release.browseId).onSuccess { albumPage ->
                database.transaction { insert(albumPage) }
                albumWithSongs = database.albumWithSongs(release.browseId).first()
            }.onFailure {
                Timber.tag(TAG).w(it, "Could not resolve album ${release.browseId} for selection")
            }
        }
        return albumWithSongs?.songs?.map { it.toMediaMetadata() }.orEmpty()
    }

    private companion object {
        const val TAG = "Zemer_LatestReleases"
    }
}
