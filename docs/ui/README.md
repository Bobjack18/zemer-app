# UI documentation

## UI stack facts

- The app module enables Jetpack Compose in `buildFeatures`.
- UI source is under `app/src/main/kotlin/com/jtech/zemer/ui`.
- The main package groups are `component`, `component/shimmer`, `menu`, `player`, `screens`, `screens/artist`, `screens/library`, `screens/player`, `screens/playlist`, `screens/search`, `screens/settings`, `screens/settings/integrations`, `theme`, and `utils`.
- Navigation uses `androidx.navigation.compose.composable` routes inside `NavigationBuilder.kt`.
- The main bottom navigation set is `Home`, `Artists`, `KidZone`, `Search`, and `Library` from `Screens.MainScreens`.

## Main screen model

| Screen object | Route | Title resource | Inactive icon | Active icon |
| --- | --- | --- | --- | --- |
| `Screens.Home` | `home` | `R.string.home` | `R.drawable.home_outlined` | `R.drawable.home_filled` |
| `Screens.Artists` | `artists` | `R.string.artists` | `R.drawable.artist` | `R.drawable.artist` |
| `Screens.KidZone` | `kid_zone` | `R.string.kid_zone` | `R.drawable.kid_zone` | `R.drawable.kid_zone` |
| `Screens.Search` | `search` | `R.string.search` | `R.drawable.search` | `R.drawable.search` |
| `Screens.Library` | `library` | `R.string.filter_library` | `R.drawable.library_music_outlined` | `R.drawable.library_music_filled` |

## Navigation routes declared in `NavigationBuilder.kt`

| Route | Destination function |
| --- | --- |
| `home` | `HomeScreen` |
| `artists` | `WhitelistedArtistsScreen` |
| `kid_zone` | `KidZoneScreen` |
| `library` | `LibraryScreen` |
| `history` | `HistoryScreen` |
| `stats` | `StatsScreen` |
| `mood_and_genres` | `MoodAndGenresScreen` |
| `account` | `AccountScreen` |
| `new_release` | `NewReleaseScreen` |
| `charts_screen` | `ChartsScreen` |
| `browse/{browseId}` | `BrowseScreen` |
| `search/{query}?filter={filter}` | `OnlineSearchResult` |
| `album/{albumId}` | `AlbumScreen` |
| `artist/{artistId}` | `ArtistScreen` |
| `artist/{artistId}/songs` | `ArtistSongsScreen` |
| `artist/{artistId}/albums` | `ArtistAlbumsScreen` |
| `artist/{artistId}/items?browseId={browseId}?params={params}` | `ArtistItemsScreen` |
| `video/{videoId}?title={title}&artist={artist}` | `VideoPlayerScreen` |
| `online_playlist/{playlistId}` | `OnlinePlaylistScreen` |
| `local_playlist/{playlistId}` | `LocalPlaylistScreen` |
| `auto_playlist/{playlist}` | `AutoPlaylistScreen` |
| `cache_playlist/{playlist}` | `CachePlaylistScreen` |
| `downloaded_content` | `DownloadedContentScreen` |
| `downloaded_videos` | `DownloadedVideosScreen` |
| `top_playlist/{top}` | `TopPlaylistScreen` |
| `youtube_browse/{browseId}?params={params}` | `YouTubeBrowseScreen` |
| `settings` | `SettingsScreen` |
| `settings/appearance` | `AppearanceSettings` |
| `settings/content` | `ContentSettings` |
| `settings/player` | `PlayerSettings` |
| `settings/general` | `GeneralSettings` |
| `settings/dpad` | `ButtonSetupScreen` |
| `settings/storage` | `StorageSettings` |
| `settings/privacy` | `PrivacySettings` |
| `settings/backup_restore` | `BackupAndRestore` |
| `settings/integrations` | `IntegrationScreen` |
| `settings/android_auto` | `AndroidAutoSettings` |
| `settings/stream_sources` | `StreamSourceSettings` |
| `settings/updater` | `UpdaterScreen` |
| `settings/about` | `AboutScreen` |
| `login` | `LoginScreen` |
| `login_gate` | `LoginGateScreen` |

## Screen groups

| Group | Files | Observable responsibility |
| --- | --- | --- |
| Root screens | `AccountScreen`, `AlbumScreen`, `BrowseScreen`, `ChartsScreen`, `ExploreScreen`, `HistoryScreen`, `HomeScreen`, `KidZoneScreen`, `LoginGateScreen`, `LoginScreen`, `MoodAndGenresScreen`, `NewReleaseScreen`, `OnboardingScreen`, `SplashScreen`, `StatsScreen`, `WhitelistedArtistsScreen`, `YouTubeBrowseScreen` | Top-level and feature screens wired from navigation or startup/auth flows. |
| Artist screens | `ArtistScreen`, `ArtistSongsScreen`, `ArtistAlbumsScreen`, `ArtistItemsScreen` | Artist detail, song, album, and extra item views. |
| Library screens | `LibraryScreen`, `LibrarySongsScreen`, `LibraryAlbumsScreen`, `LibraryArtistsScreen`, `LibraryPlaylistsScreen`, `LibraryMixScreen`, `LibraryVideosScreen` | Local/library tabs and media groupings. |
| Playlist screens | `AutoPlaylistScreen`, `CachePlaylistScreen`, `DownloadedContentScreen`, `DownloadedVideosScreen`, `LocalPlaylistScreen`, `OnlinePlaylistScreen`, `TopPlaylistScreen` | Playlist, cache, downloaded, online, and ranked media views. |
| Search screens | `OnlineSearchScreen`, `OnlineSearchResult` | Search suggestions and search result presentation. |
| Settings screens | `SettingsScreen`, `AccountSettings`, `AndroidAutoSettings`, `AppearanceSettings`, `BackupAndRestore`, `ButtonSetupScreen`, `ContentSettings`, `ContributeScreen`, `GeneralSettings`, `PlayerSettings`, `PrivacySettings`, `StorageSettings`, `StreamSourceSettings`, `UpdaterSettings`, `integrations/IntegrationScreen` | Settings hub and individual settings/detail flows. |
| Player UI | `player/*.kt`, `screens/player/VideoPlayerScreen.kt` | Now-playing surface, lyrics, queue, thumbnails, controls, and video playback screen. |
| Reusable components | `component/*.kt`, `component/shimmer/*.kt`, `menu/*.kt`, `utils/*.kt` | Cards, list/grid rows, dialogs, menus, app bars, layout utilities, shimmer placeholders, and support components. |
| Theme | `theme/*.kt` | Compose colors, typography/theme wrapper, slider colors, and dynamic color helpers. |

## Composable inventory

| File | Lines | Composable declarations found |
| --- | ---: | --- |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AccountSettingsDialog.kt` | 65 | AccountSettingsDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AnonymousAuthEmailDialog.kt` | 123 | AnonymousAuthEmailDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AppBarSearchField.kt` | 55 | AppBarSearchField |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AppStateViews.kt` | 111 | AppStateView |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AutoResizeText.kt` | 97 | AutoResizeText |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BigSeekBar.kt` | 87 | BigSeekBar |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheet.kt` | 348 | BottomSheet, rememberBottomSheetState |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheetMenu.kt` | 86 | show, dismiss, BottomSheetMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheetPage.kt` | 166 | show, dismiss, BottomSheetPage |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/ChipsRow.kt` | 246 | ChipsRow, ChoiceChipsRow |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/CreatePlaylistDialog.kt` | 129 | CreatePlaylistDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Dialog.kt` | 367 | DefaultDialog, ActionPromptDialog, ListDialog, InfoLabel, TextFieldDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/DraggableScrollBarOverlay.kt` | 242 | DraggableScrollbar |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/EmptyPlaceholder.kt` | 47 | EmptyPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/GridMenu.kt` | 197 | GridMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/HideOnScrollFAB.kt` | 117 | HideOnScrollFAB, HideOnScrollFAB, HideOnScrollFAB |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/IconButton.kt` | 131 | ResizableIconButton, IconButton |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/InfoCard.kt` | 100 | InfoCard, StatusRow |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Items.kt` | 1619 | ListItem, ListItem, GridItem, GridItem, SongListItem, SongGridItem, ArtistListItem, ArtistGridItem, AlbumListItem, AlbumGridItem, PlaylistListItem, PlaylistGridItem, MediaMetadataListItem, YouTubeListItem, YouTubeGridItem, LocalSongsGrid, LocalArtistsGrid, LocalAlbumsGrid, ItemThumbnail, LocalThumbnail, PlaylistThumbnail, OverlayPlayButton, OverlayEditButton, AlbumPlayButton, SwipeToSongBox, Favorite, Library, Download, Download, Explicit |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Library.kt` | 411 | LibraryArtistListItem, WhitelistedArtistListItem, LibraryArtistGridItem, WhitelistedArtistGridItem, LibraryAlbumListItem, LibraryAlbumGridItem, LibraryPlaylistListItem, LibraryPlaylistGridItem |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Lyrics.kt` | 998 | Lyrics |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/LyricsImageCard.kt` | 287 | rememberAdjustedFontSize, LyricsImageCard |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NavigationTile.kt` | 59 | NavigationTile |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NavigationTitle.kt` | 77 | NavigationTitle |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NetworkRequiredDialog.kt` | 100 | NetworkRequiredDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NewMenuComponents.kt` | 319 | NewActionButton, NewMenuItem, NewMenuSectionHeader, NewActionGrid, NewMenuContent, NewIconButton, NewMenuContainer |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/PlayerSlider.kt` | 112 | PlayerSliderTrack |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/PlayingIndicator.kt` | 114 | PlayingIndicator, PlayingIndicatorBox |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Preference.kt` | 366 | PreferenceEntry, ListPreference, SwitchPreference, EditTextPreference, SliderPreference, PreferenceGroupTitle |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/SearchBar.kt` | 371 | TopSearch, SearchBarInputField |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/SelectPreference.kt` | 71 | SelectPreference |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/WebViewAuthDialog.kt` | 130 | WebViewAuthDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ButtonPlaceholder.kt` | 21 | ButtonPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/GridItemPlaceholder.kt` | 56 | GridItemPlaceHolder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ListItemPlaceholder.kt` | 53 | ListItemPlaceHolder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ShimmerHost.kt` | 64 | ShimmerHost |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/TextPlaceholder.kt` | 33 | TextPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AddToPlaylistDialog.kt` | 191 | AddToPlaylistDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AddToPlaylistDialogOnline.kt` | 281 | AddToPlaylistDialogOnline |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AlbumMenu.kt` | 673 | AlbumMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/ArtistMenu.kt` | 361 | ArtistMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/CustomThumbnailMenu.kt` | 70 | CustomThumbnailMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/ImportPlaylistDialog.kt` | 63 | ImportPlaylistDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/LoadingScreen.kt` | 37 | LoadingScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/LyricsMenu.kt` | 378 | LyricsMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/PlayerMenu.kt` | 740 | PlayerMenu, TempoPitchDialog, ValueAdjuster |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/PlaylistMenu.kt` | 331 | PlaylistMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/SelectionSongsMenu.kt` | 898 | SelectionSongMenu, SelectionMediaMetadataMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/SongMenu.kt` | 895 | SongMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeAlbumMenu.kt` | 607 | YouTubeAlbumMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeArtistMenu.kt` | 316 | YouTubeArtistMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubePlaylistMenu.kt` | 563 | YouTubePlaylistMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeSongMenu.kt` | 747 | YouTubeSongMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/LyricsScreen.kt` | 797 | LyricsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/MiniPlayer.kt` | 911 | MiniPlayer, NewMiniPlayer, LegacyMiniPlayer, LegacyMiniMediaInfo |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/PlaybackError.kt` | 39 | PlaybackError |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Player.kt` | 1382 | BottomSheetPlayer, BottomSheetPlayerPreview |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Queue.kt` | 1162 | Queue |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Thumbnail.kt` | 471 | Thumbnail |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/AccountScreen.kt` | 200 | AccountScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/AlbumScreen.kt` | 723 | AlbumScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/BrowseScreen.kt` | 144 | BrowseScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/ChartsScreen.kt` | 306 | ChartsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/ExploreScreen.kt` | 399 | ExploreScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/HistoryScreen.kt` | 497 | HistoryScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/HomeScreen.kt` | 1037 | HomeScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/KidZoneScreen.kt` | 335 | KidZoneScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/LoginGateScreen.kt` | 253 | LoginGateScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/LoginScreen.kt` | 227 | LoginScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/MoodAndGenresScreen.kt` | 170 | MoodAndGenresScreen, MoodAndGenresButton |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/NewReleaseScreen.kt` | 254 | NewReleaseScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/OnboardingScreen.kt` | 2074 | NetworkStatusBanner, OnboardingFlow, WelcomeScreen, DensityScreen, RestartDialog, CustomDensityDialog, ContentFiltersScreen, FilterOptionCard, PermissionsScreen, PermissionCard, LegalOverlay, LoadingScreen, DisposableLifecycle, DisposableEffectWithLifecycle, BottomNavSetupScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/SplashScreen.kt` | 164 | SplashScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/StatsScreen.kt` | 426 | StatsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/WhitelistedArtistsScreen.kt` | 403 | WhitelistedArtistsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/YouTubeBrowseScreen.kt` | 285 | YouTubeBrowseScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistAlbumsScreen.kt` | 158 | ArtistAlbumsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistItemsScreen.kt` | 330 | ArtistItemsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistScreen.kt` | 856 | ArtistScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistSongsScreen.kt` | 212 | ArtistSongsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryAlbumsScreen.kt` | 323 | LibraryAlbumsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryArtistsScreen.kt` | 301 | LibraryArtistsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryMixScreen.kt` | 797 | LibraryMixScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryPlaylistsScreen.kt` | 545 | LibraryPlaylistsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryScreen.kt` | 87 | LibraryScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibrarySongsScreen.kt` | 355 | LibrarySongsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryVideosScreen.kt` | 155 | LibraryVideosScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/player/VideoPlayerScreen.kt` | 1160 | VideoPlayerScreen, formatTime |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/AutoPlaylistScreen.kt` | 659 | AutoPlaylistScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/CachePlaylistScreen.kt` | 469 | CachePlaylistScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/DownloadedContentScreen.kt` | 181 | DownloadedContentScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/DownloadedVideosScreen.kt` | 489 | DownloadedVideosScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/LocalPlaylistScreen.kt` | 1512 | LocalPlaylistScreen, LocalPlaylistHeader |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/OnlinePlaylistScreen.kt` | 709 | OnlinePlaylistScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/TopPlaylistScreen.kt` | 610 | TopPlaylistScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/search/OnlineSearchResult.kt` | 441 | OnlineSearchResult |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/search/OnlineSearchScreen.kt` | 459 | OnlineSearchScreen, SuggestionItem |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AboutScreen.kt` | 192 | AboutScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AccountSettings.kt` | 451 | AccountSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AndroidAutoSettings.kt` | 290 | label, AndroidAutoSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AppearanceSettings.kt` | 1034 | AppearanceSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/BackupAndRestore.kt` | 191 | BackupAndRestore |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ButtonSetupScreen.kt` | 374 | ButtonSetupScreen, ListeningOverlay, CompletedCard, AssignmentRow, DpadDirectionIcon, PrimingOverlay, AccessibilityPermissionRequired |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ContentSettings.kt` | 675 | ContentSettings, SyncStatusCard |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ContributeScreen.kt` | 392 | ContributeScreen, ProgressRow, ProfilePrompt, ArtistTaskCard, ToggleRow |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/GeneralSettings.kt` | 89 | GeneralSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/PlayerSettings.kt` | 247 | PlayerSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/PrivacySettings.kt` | 209 | PrivacySettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/SettingsScreen.kt` | 265 | SettingsScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/StorageSettings.kt` | 441 | StorageSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/StreamSourceSettings.kt` | 218 | StreamSourceSettings |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/UpdaterSettings.kt` | 264 | UpdaterScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/integrations/IntegrationScreen.kt` | 51 | IntegrationScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/PlayerSliderColors.kt` | 146 | getSliderColors, defaultSliderColors, squigglySliderColors, slimSliderColors |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Theme.kt` | 113 | ZemerTheme |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/AppBar.kt` | 75 | appBarScrollBehavior |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ScrollUtils.kt` | 59 | isScrollingUp, isScrollingUp, isScrollingUp |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ShowMediaInfo.kt` | 342 | ShowMediaInfo |

## UI Kotlin file inventory

| File | Lines | Key declarations |
| --- | ---: | --- |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AccountSettingsDialog.kt` | 65 | fun AccountSettingsDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AnonymousAuthEmailDialog.kt` | 123 | fun AnonymousAuthEmailDialog, val coroutineScope, var isLoading |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AppBarSearchField.kt` | 55 | fun AppBarSearchField |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AppStateViews.kt` | 111 | fun AppStateView |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/AutoResizeText.kt` | 97 | fun AutoResizeText, var fontSizeValue, var readyToDraw, val nextFontSizeValue, class FontSizeRange, val min, val max, val step, val DEFAULT_TEXT_STEP |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BigSeekBar.kt` | 87 | val SeekKeyStep, fun BigSeekBar, var width, val interactionSource, val focused |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheet.kt` | 348 | fun BottomSheet, val y, val velocityTracker, val velocity, class BottomSheetState, val coroutineScope, val animatable, val onAnchorChanged, val collapsedBound, val dismissedBound, val expandedBound, val value, … +33 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheetMenu.kt` | 86 | val LocalMenuState, class MenuState, var isVisible, var content, fun show, fun dismiss, fun BottomSheetMenu, val focusManager |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/BottomSheetPage.kt` | 166 | val LocalBottomSheetPageState, class BottomSheetPageState, var isVisible, var content, fun show, fun dismiss, fun BottomSheetPage, val focusManager, val coroutineScope, var dragOffset |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/ChipsRow.kt` | 246 | var isFocused, val borderColor, var expandIconDegree, val rotationAnimation, var expanded, var isFocused, val borderColor, var isFocused, val borderColor |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/CreatePlaylistDialog.kt` | 129 | fun CreatePlaylistDialog, val database, val coroutineScope, var syncedPlaylist, val context, val isSignedIn, val isSyncEnabled, val browseId |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Dialog.kt` | 367 | fun DefaultDialog, fun ActionPromptDialog, fun ListDialog, fun InfoLabel, fun TextFieldDialog, val legacyFieldState, val focusRequester, val isValid |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/DraggableScrollBarOverlay.kt` | 242 | fun DraggableScrollbar, val density, val coroutineScope, var isDragging, var lastScrollTime, var smoothedY, var smoothedThumbY, var lastThumbPosition, val animatedThumbY, val isUserScrolling, val isScrollable, val layoutInfo, … +39 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/EmptyPlaceholder.kt` | 47 | fun EmptyPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/GridMenu.kt` | 197 | val GridMenuItemHeight, fun GridMenu, fun LazyGridScope, fun LazyGridScope, fun LazyGridScope, fun LazyGridScope |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/HideOnScrollFAB.kt` | 117 | fun BoxScope, fun BoxScope, fun BoxScope |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/IconButton.kt` | 131 | fun ResizableIconButton, val isFocused, val borderColor, val bgColor, fun IconButton, val isFocused, val borderColor, val bgColor, val contentColor |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/InfoCard.kt` | 100 | fun InfoCard, fun StatusRow |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Items.kt` | 1619 | val ActiveBoxAlpha, fun ListItem, var isFocused, val backgroundColor, val borderColor, fun ListItem, fun GridItem, var isFocused, val backgroundColor, val borderColor, val baseModifier, fun GridItem, … +80 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Library.kt` | 411 | fun LibraryArtistListItem, fun WhitelistedArtistListItem, fun LibraryArtistGridItem, fun WhitelistedArtistGridItem, fun LibraryAlbumListItem, fun LibraryAlbumGridItem, fun LibraryPlaylistListItem, fun LibraryPlaylistGridItem |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Lyrics.kt` | 998 | fun Lyrics, val playerConnection, val density, val context, val configuration, val landscapeOffset, val lyricsTextPosition, val changeLyrics, val scrollLyrics, val scope, val mediaMetadata, val lyricsEntity, … +100 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/LyricsImageCard.kt` | 287 | fun rememberAdjustedFontSize, val measurer, var calculatedFontSize, val initialSize, val targetWidthPx, val targetHeightPx, val largerSize, val result, val largerSize, val result, var minSize, var maxSize, … +22 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NavigationTile.kt` | 59 | fun NavigationTile |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NavigationTitle.kt` | 77 | fun NavigationTitle |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NetworkRequiredDialog.kt` | 100 | fun NetworkRequiredDialog, val context, var isRetrying, var currentConnectionState |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/NewMenuComponents.kt` | 319 | fun NewActionButton, var isFocused, val animatedBackground, val animatedContent, val borderColor, fun NewMenuItem, var isFocused, val backgroundColor, val borderColor, fun NewMenuSectionHeader, fun NewActionGrid, val rows, … +13 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/PlayerSlider.kt` | 112 | fun PlayerSliderTrack, val inactiveTrackColor, val activeTrackColor, val inactiveTickColor, val activeTickColor, val valueRange, fun DrawScope, val isRtl, val sliderLeft, val sliderRight, val sliderStart, val sliderEnd, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/PlayingIndicator.kt` | 114 | fun PlayingIndicator, val animatables, fun PlayingIndicatorBox |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/Preference.kt` | 366 | fun PreferenceEntry, var isFocused, val backgroundColor, val borderColor, var showDialog, fun SwitchPreference, fun EditTextPreference, var showDialog, fun SliderPreference, var showDialog, var sliderValue, fun PreferenceGroupTitle |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/SearchBar.kt` | 371 | fun TopSearch, val animationProgress, val defaultInputFieldShape, val defaultFullScreenShape, val animatedShape, val animatedRadius, val topInset, val startInset, val endInset, val topPadding, val animatedSurfaceTopPadding, val animatedInputFieldPadding, … +20 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/SelectPreference.kt` | 71 | var showDialog |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/SortHeader.kt` | 106 | var menuExpanded |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/WebViewAuthDialog.kt` | 130 | fun WebViewAuthDialog, val context, val coroutineScope, var isLoading |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ButtonPlaceholder.kt` | 21 | fun ButtonPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/GridItemPlaceholder.kt` | 56 | fun GridItemPlaceHolder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ListItemPlaceholder.kt` | 53 | fun ListItemPlaceHolder |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/ShimmerHost.kt` | 64 | fun ShimmerHost, val ShimmerTheme |
| `app/src/main/kotlin/com/jtech/zemer/ui/component/shimmer/TextPlaceholder.kt` | 33 | fun TextPlaceholder |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AddToPlaylistDialog.kt` | 191 | fun AddToPlaylistDialog, val database, val coroutineScope, var playlists, val _, var showCreatePlaylistDialog, var showDuplicateDialog, var selectedPlaylist, var songIds, var duplicates |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AddToPlaylistDialogOnline.kt` | 281 | fun AddToPlaylistDialogOnline, val database, val coroutineScope, var playlists, var showCreatePlaylistDialog, var showDuplicateDialog, var selectedPlaylist, val songIds, val duplicates, fun findFirstSong, val allArtists, val query, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/AlbumMenu.kt` | 673 | fun AlbumMenu, val context, val database, val downloadUtil, val playerConnection, val scope, val libraryAlbum, val album, var songs, val auth, val firestore, var showReportDialog, … +26 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/ArtistMenu.kt` | 361 | fun ArtistMenu, val context, val auth, val firestore, var showReportDialog, var selectedReason, var comment, var isSubmitting, val database, val playerConnection, val artistState, val artist, … +8 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/CustomThumbnailMenu.kt` | 70 | fun CustomThumbnailMenu |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/ImportPlaylistDialog.kt` | 63 | fun ImportPlaylistDialog, val database, val coroutineScope, val textFieldValue, var songIds, val newPlaylist, val playlist |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/LoadingScreen.kt` | 37 | fun LoadingScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/LyricsMenu.kt` | 378 | fun LyricsMenu, val context, val database, var showEditDialog, var showSearchDialog, var showSearchResultDialog, val searchMediaMetadata, val titleField, val onTitleFieldChange, val artistField, val onArtistFieldChange, val isNetworkAvailable, … +6 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/PlayerMenu.kt` | 740 | fun PlayerMenu, val context, val auth, val firestore, val database, val playerConnection, val playerVolume, val activityResultLauncher, val coroutineScope, val downloadUtil, val mediaStoreDownload, val download, … +23 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/PlaylistMenu.kt` | 331 | fun PlaylistMenu, val context, val database, val downloadUtil, val playerConnection, val auth, val firestore, val dbPlaylist, var songs, var showReportDialog, var selectedReason, var comment, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/SelectionSongsMenu.kt` | 898 | fun SelectionSongMenu, val context, val database, val downloadUtil, val coroutineScope, val playerConnection, val syncUtils, val auth, val firestore, var showReportDialog, var selectedReason, var comment, … +31 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/SongMenu.kt` | 895 | fun SongMenu, val context, val database, val playerConnection, val songState, val song, val downloadUtil, val mediaStoreDownload, val coroutineScope, val syncUtils, val scope, var refetchIconDegree, … +39 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeAlbumMenu.kt` | 607 | fun YouTubeAlbumMenu, val context, val auth, val firestore, val database, val downloadUtil, val playerConnection, val album, val coroutineScope, var downloadState, val songs, var showChoosePlaylistDialog, … +15 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeArtistMenu.kt` | 316 | fun YouTubeArtistMenu, val context, val auth, val firestore, val scope, var showReportDialog, var selectedReason, var comment, var isSubmitting, val database, val playerConnection, val libraryArtist, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubePlaylistMenu.kt` | 563 | fun YouTubePlaylistMenu, val context, val database, val downloadUtil, val playerConnection, val dbPlaylist, var showChoosePlaylistDialog, var showImportPlaylistDialog, var showErrorPlaylistAddDialog, val notAddedList, val allSongs, val playlistEntity, … +8 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/menu/YouTubeSongMenu.kt` | 747 | fun YouTubeSongMenu, val context, val auth, val firestore, val database, val playerConnection, val downloadUtil, val librarySong, val mediaStoreDownload, val download, val coroutineScope, val syncUtils, … +26 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/LyricsScreen.kt` | 797 | fun LyricsScreen, val context, val activity, val playerConnection, val player, val menuState, val database, val coroutineScope, val playbackState, val isPlaying, val repeatMode, val shuffleModeEnabled, … +23 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/MiniPlayer.kt` | 911 | class MiniPlayerFocusTargets, val play, val account, val heart, val afterHeart, val down, fun Modifier, fun MiniPlayer, val useNewMiniPlayerDesign, fun NewMiniPlayer, val playerConnection, val database, … +74 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/PlaybackError.kt` | 39 | fun PlaybackError |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Player.kt` | 1382 | fun BottomSheetPlayer, val context, val clipboardManager, val menuState, val bottomSheetPageState, val playerConnection, val useNewPlayerDesign, val _, val floatingMiniPlayerPref, val _, val floatingMiniPlayerEnabled, val playerBackground, … +98 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Queue.kt` | 1162 | fun Queue, val context, val haptic, val menuState, val bottomSheetPageState, val playerConnection, val isPlaying, val repeatMode, val currentWindowIndex, val mediaMetadata, val selectedSongs, val selectedItems, … +39 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/player/Thumbnail.kt` | 471 | fun Thumbnail, val playerConnection, val context, val mediaMetadata, val error, val queueTitle, val swipeThumbnail, val hidePlayerThumbnail, val canSkipPrevious, val canSkipNext, val playerBackground, val textBackgroundColor, … +44 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/AccountScreen.kt` | 200 | fun AccountScreen, val menuState, val haptic, val coroutineScope, val playlists, val albums, val artists, val selectedContentType |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/AlbumScreen.kt` | 723 | fun AlbumScreen, val context, val menuState, val database, val haptic, val coroutineScope, val playerConnection, val isPlaying, val mediaMetadata, val playlistId, val albumWithSongs, val hideExplicit, … +34 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/BrowseScreen.kt` | 144 | fun BrowseScreen, val menuState, val playerConnection, val isPlaying, val title, val items, val coroutineScope |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/ChartsScreen.kt` | 306 | fun ChartsScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val chartsPage, val isLoading, val lazyListState, val horizontalLazyGridItemWidthFactor, val horizontalLazyGridItemWidth, … +4 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/ExploreScreen.kt` | 399 | fun ExploreScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val explorePage, val chartsPage, val isChartsLoading, val coroutineScope, val scrollState, … +8 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/HistoryScreen.kt` | 497 | fun HistoryScreen, val context, val database, val menuState, val haptic, val playerConnection, val isPlaying, val mediaMetadata, var selection, var isSearching, var query, val focusRequester, … +19 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/HomeScreen.kt` | 1037 | fun HomeScreen, val viewModel, val menuState, val database, val playerConnection, val haptic, val context, val isPlaying, val mediaMetadata, val homeUiState, val quickPicks, val featuredPlaylists, … +51 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/KidZoneScreen.kt` | 335 | fun KidZoneScreen, val menuState, var viewType, val firstFocus, val searchFocus, val firstArtistFocus, val artists, val searchQuery, val syncProgress, val isSyncing, val coroutineScope, var showSyncOverlay, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/LoginGateScreen.kt` | 253 | fun LoginGateScreen, val context, val coroutineScope, var isAnonymousLoading, var visitorData, var dataSyncId, var innerTubeCookie, var accountName, var accountEmail, var accountChannelHandle, val gradient, val httpClient, … +11 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/LoginScreen.kt` | 227 | fun LoginScreen, val context, val coroutineScope, var visitorData, var dataSyncId, var innerTubeCookie, var accountName, var accountEmail, var accountChannelHandle, var hasCompletedLogin, var webView, fun shouldOverrideUrlLoading, … +8 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/MoodAndGenresScreen.kt` | 170 | fun MoodAndGenresScreen, val localConfiguration, val itemsPerRow, val moodAndGenresList, val isLoading, val error, fun MoodAndGenresButton, val MoodAndGenresButtonHeight |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/NavigationBuilder.kt` | 346 | fun NavGraphBuilder, val videoId, val title, val artist |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/NewReleaseScreen.kt` | 254 | fun NewReleaseScreen, val menuState, val haptic, val playerConnection, val database, val isPlaying, val mediaMetadata, val newReleaseAlbums, val newReleaseSongs, val isLoading, val error, val coroutineScope |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/OnboardingScreen.kt` | 2074 | class OnboardingStep, class LegalKind, fun NetworkStatusBanner, val context, var isConnected, var isChecking, val newConnectionState, fun OnboardingFlow, val context, val viewModel, val uiState, val densityAlreadySet, … +101 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/Screens.kt` | 53 | class Screens, val titleId, val iconIdInactive, val iconIdActive, val route, object Home, object Artists, object KidZone, object Search, object Library, val MainScreens |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/SplashScreen.kt` | 164 | fun SplashScreen, var hasTappedSkip, val composition, val lottieColors, val loopingState |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/StatsScreen.kt` | 426 | fun StatsScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val context, val indexChips, val mostPlayedSongs, val mostPlayedSongsStats, val mostPlayedArtists, … +22 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/VideoNavigation.kt` | 12 | fun videoRoute, val params, val query |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/WhitelistedArtistsScreen.kt` | 403 | fun WhitelistedArtistsScreen, val menuState, var viewType, val _, val firstFocus, val searchFocus, val firstArtistFocus, val artists, val searchQuery, val syncProgress, val isSyncing, val coroutineScope, … +9 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/YouTubeBrowseScreen.kt` | 285 | fun YouTubeBrowseScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val browseResult, val coroutineScope, val horizontalLazyGridItemWidthFactor, val lazyGridState, val snapLayoutInfoProvider |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistAlbumsScreen.kt` | 158 | fun ArtistAlbumsScreen, val menuState, val playerConnection, val isPlaying, val mediaMetadata, val artist, val albums, val coroutineScope, val lazyGridState, var inSelectMode, val selection, val onExitSelectionMode, … +1 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistItemsScreen.kt` | 330 | fun ArtistItemsScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val lazyListState, val lazyGridState, val coroutineScope, val blockVideos, val _, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistScreen.kt` | 856 | fun ArtistScreen, val context, val database, val menuState, val haptic, val coroutineScope, val playerConnection, val isPlaying, val mediaMetadata, val artistPage, val isLoadingArtist, val libraryArtist, … +33 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/artist/ArtistSongsScreen.kt` | 212 | fun ArtistSongsScreen, val context, val menuState, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val artist, … +2 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryAlbumsScreen.kt` | 323 | fun LibraryAlbumsScreen, val menuState, val playerConnection, val isPlaying, val mediaMetadata, var viewType, var filter, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val gridItemSize, … +12 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryArtistsScreen.kt` | 301 | fun LibraryArtistsScreen, val menuState, var viewType, var filter, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val gridItemSize, val ytmSync, val filterContent, val artists, … +6 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryMixScreen.kt` | 797 | fun LibraryMixScreen, val menuState, val haptic, val playerConnection, val isPlaying, val mediaMetadata, var viewType, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val gridItemSize, … +26 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryPlaylistsScreen.kt` | 545 | fun LibraryPlaylistsScreen, val menuState, val coroutineScope, var viewType, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val gridItemSize, val playlists, val topSize, val autoPlaylistsState, … +20 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryScreen.kt` | 87 | fun LibraryScreen, var filterType, val blockVideos, val _, val availableFilters, val filterContent |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibrarySongsScreen.kt` | 355 | fun LibrarySongsScreen, val context, val menuState, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, val ytmSync, … +11 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/library/LibraryVideosScreen.kt` | 155 | fun LibraryVideosScreen, val menuState, val haptic, val playerConnection, val database, val isPlaying, val mediaMetadata, val videos, val lazyListState, val artistDisplay |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/player/VideoPlayerScreen.kt` | 1160 | fun VideoPlayerScreen, val context, val blockVideos, val _, val activity, val clipboard, val connectivityManager, val database, val scope, val playerConnection, val lifecycleOwner, var videoItem, … +116 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/AutoPlaylistScreen.kt` | 659 | fun AutoPlaylistScreen, val context, val coroutineScope, val menuState, val haptic, val focusManager, val playerConnection, val isPlaying, val mediaMetadata, val playlist, val songs, val mutableSongs, … +22 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/CachePlaylistScreen.kt` | 469 | fun CachePlaylistScreen, val menuState, val playerConnection, val haptic, val focusManager, val isPlaying, val mediaMetadata, val cachedSongs, val sortType, val onSortTypeChange, val sortDescending, val onSortDescendingChange, … +11 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/DownloadedContentScreen.kt` | 181 | fun DownloadedContentScreen, val blockVideos, val _, val musicCount, val videoCount |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/DownloadedVideosScreen.kt` | 489 | fun DownloadedVideosScreen, val menuState, val haptic, val focusManager, val playerConnection, val isPlaying, val mediaMetadata, val videos, val mutableVideos, var isSearching, var query, val focusRequester, … +15 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/LocalPlaylistScreen.kt` | 1512 | fun LocalPlaylistScreen, val context, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val playlist, val songs, val mutableSongs, val sortType, … +81 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/OnlinePlaylistScreen.kt` | 709 | fun OnlinePlaylistScreen, val menuState, val database, val haptic, val playerConnection, val isPlaying, val mediaMetadata, val playlist, val songs, val dbPlaylist, val isLoading, val isLoadingMore, … +18 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/playlist/TopPlaylistScreen.kt` | 610 | fun TopPlaylistScreen, val coroutineScope, val context, val menuState, val haptic, val focusManager, val playerConnection, val isPlaying, val mediaMetadata, val maxSize, val songs, val mutableSongs, … +16 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/search/OnlineSearchResult.kt` | 441 | fun OnlineSearchResult, val menuState, val database, val playerConnection, val haptic, val isPlaying, val mediaMetadata, val coroutineScope, val lazyListState, val chipsFocusRequester, val firstResultFocusRequester, val searchFilter, … +16 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/search/OnlineSearchScreen.kt` | 459 | fun OnlineSearchScreen, val database, val keyboardController, val menuState, val playerConnection, val scope, val haptic, val isPlaying, val mediaMetadata, val coroutineScope, val viewState, val lazyListState, … +7 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AboutScreen.kt` | 192 | fun AboutScreen, val uriHandler, val backFocus, val firstFocus |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AccountSettings.kt` | 451 | fun AccountSettings, val context, val uriHandler, val accountNamePref, val onAccountNameChange, val accountEmail, val onAccountEmailChange, val accountChannelHandle, val onAccountChannelHandleChange, val innerTubeCookie, val onInnerTubeCookieChange, val visitorData, … +33 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AndroidAutoSettings.kt` | 290 | class AndroidAutoSection, val id, fun AndroidAutoSection, fun AndroidAutoSection, fun serializeSections, fun deserializeSections, val parsed, val parts, val section, val enabled, val present, val missing, … +19 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/AppearanceSettings.kt` | 1034 | fun AppearanceSettings, val dynamicTheme, val onDynamicThemeChange, val darkMode, val onDarkModeChange, val useNewPlayerDesign, val onUseNewPlayerDesignChange, val useNewMiniPlayerDesign, val onUseNewMiniPlayerDesignChange, val floatingMiniPlayerEnabled, val onFloatingMiniPlayerEnabledChange, val hidePlayerThumbnail, … +81 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/BackupAndRestore.kt` | 191 | fun BackupAndRestore, var importedTitle, val importedSongs, var showChoosePlaylistDialogOnline, var isProgressStarted, var progressPercentage, val context, val backupLauncher, val restoreLauncher, val importPlaylistFromCsv, val result, val importM3uLauncherOnline, … +4 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ButtonSetupScreen.kt` | 374 | fun ButtonSetupScreen, val viewModel, val accessibilityEnabled, val context, val focusManager, val uiState, val scrollState, val currentStep, val showOverlay, val step, fun ListeningOverlay, fun CompletedCard, … +5 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ContentSettings.kt` | 675 | class ContentSettingsViewModel, val authManager, val webAuthManager, val syncService, val userPreferencesRepository, val authState, val syncState, val syncStatus, fun signInWithGoogle, fun signInAnonymously, fun signOut, fun performManualSync, … +54 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/ContributeScreen.kt` | 392 | fun ContributeScreen, val uiState, val context, val credentialManager, val scope, val scrollState, fun launchGoogleSignIn, val googleIdOption, val request, val response, val credential, val googleIdTokenCredential, … +9 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/GeneralSettings.kt` | 89 | fun GeneralSettings, val context, val intent, val intent |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/PlayerSettings.kt` | 247 | fun PlayerSettings, val audioQuality, val onAudioQualityChange, val persistentQueue, val onPersistentQueueChange, val skipSilence, val onSkipSilenceChange, val audioNormalization, val onAudioNormalizationChange, val audioOffload, val onAudioOffloadChange, val seekExtraSeconds, … +15 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/PrivacySettings.kt` | 209 | fun PrivacySettings, val database, val pauseListenHistory, val onPauseListenHistoryChange, val pauseSearchHistory, val onPauseSearchHistoryChange, val disableScreenshot, val onDisableScreenshotChange, var showClearListenHistoryDialog, var showClearSearchHistoryDialog, val backFocus, val firstFocus |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/SettingsScreen.kt` | 265 | class SettingItem, val id, val title, val description, val icon, val section, val route, fun SettingsScreen, val context, val hasAndroidAuto, val firebaseAuth, var isLoggedIn, … +9 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/StorageSettings.kt` | 441 | fun StorageSettings, val context, val imageDiskCache, val playerService, val playerCache, val downloadCache, val database, val coroutineScope, val maxImageCacheSize, val onMaxImageCacheSizeChange, val maxSongCacheSize, val onMaxSongCacheSizeChange, … +23 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/StreamSourceSettings.kt` | 218 | fun StreamSourceSettings, val webRemixEnabled, val onWebRemixChange, val tvhtml5Enabled, val onTVHTML5Change, val androidVREnabled, val onAndroidVRChange, val iosEnabled, val onIOSChange, val ipadosEnabled, val onIPadOSChange, val visionosEnabled, … +8 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/UpdaterSettings.kt` | 264 | fun UpdaterScreen, val context, val scope, val checkForUpdates, val onCheckForUpdatesChange, var isChecking, var showResultDialog, var updateResult, var downloadState, val backFocus, val firstFocus, val apkFile, … +4 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/screens/settings/integrations/IntegrationScreen.kt` | 51 | fun IntegrationScreen |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/AppColors.kt` | 23 | object AppColors, val scrim, val onMedia, fun mediaOverlay, fun onMedia |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Dimens.kt` | 26 | object Dimens, val space1, val space2, val space3, val space4, val space6, val space8, val ScreenPaddingH, val ItemGap, val IconSize, val MinTouchTarget, val DialogPadding |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Motion.kt` | 28 | object Motion, val ShortMs, val MediumMs, val LongMs |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/PlayerColorExtractor.kt` | 159 | object PlayerColorExtractor, fun extractGradientColors, val colorCandidates, val bestSwatch, val fallbackDominant, val primaryColor, val bestColor, fun isColorVibrant, val argb, val hsv, val saturation, val brightness, … +30 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/PlayerSliderColors.kt` | 146 | object PlayerSliderColors, fun getSliderColors, val inactiveTrackColor, fun defaultSliderColors, fun squigglySliderColors, fun slimSliderColors, val inactiveTrackColor, object Config, val INACTIVE_TRACK_ALPHA, val INACTIVE_TICK_ALPHA, val DEFAULT_ACTIVE_COLOR, val DEFAULT_INACTIVE_COLOR |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Shape.kt` | 27 | val AppShapes, val PillShape |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Theme.kt` | 113 | val DefaultThemeColor, fun ZemerTheme, val context, val useSystemDynamicColor, val baseColorScheme, val neutralDefaults, val mergedColorScheme, val colorScheme, fun Bitmap, val colorsToPopulation, val rankedColors, fun Bitmap, … +6 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/theme/Type.kt` | 123 | val AppTypography |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/AppBar.kt` | 75 | fun appBarScrollBehavior, class AppBarScrollBehavior, val state, val snapAnimationSpec, val flingAnimationSpec, val canScroll, val isPinned, var nestedScrollConnection, fun onPostScroll, fun TopAppBarState |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/FadingEdge.kt` | 89 | fun Modifier, fun Modifier |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ItemWrapper.kt` | 15 | class ItemWrapper, val item, val _isSelected, var isSelected |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/KeyUtils.kt` | 50 | object KeyUtils, val counter, fun generateUniqueKey, val uniqueId, fun generateIndexedKey, val uniqueId, fun generateTimestampKey, val timestamp, val uniqueId |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/LazyGridSnapLayoutInfoProvider.kt` | 66 | fun SnapLayoutInfoProvider, val layoutInfo, fun calculateApproachOffset, fun calculateSnapOffset, val bounds, fun calculateSnappingOffsetBounds, var lowerBoundOffset, var upperBoundOffset, val offset, fun calculateDistanceToDesiredSnapPosition, val containerSize, val desiredDistance, … +2 more |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/NavControllerUtils.kt` | 14 | fun NavController, val mainRoutes |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ScrollUtils.kt` | 59 | fun LazyListState, var previousIndex, var previousScrollOffset, fun LazyGridState, var previousIndex, var previousScrollOffset, fun ScrollState, var previousScrollOffset |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ShapeUtils.kt` | 8 | fun CornerBasedShape |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/ShowMediaInfo.kt` | 342 | fun ShowMediaInfo, val windowInsets, var info, val database, var song, var currentFormat, val playerConnection, val context, val baseList, val extendedList, val displayText, val cm |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/StringUtils.kt` | 36 | fun formatFileSize, val prefix, var result, var suffix, fun numberFormatter |
| `app/src/main/kotlin/com/jtech/zemer/ui/utils/YouTubeUtils.kt` | 23 | fun String, val W, val H, var w, var h |
