package com.jtech.zemer.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Slider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.jtech.zemer.LocalPlayerAwareWindowInsets
import com.jtech.zemer.LocalPlayerConnection
import com.jtech.zemer.R
import com.jtech.zemer.constants.AudioNormalizationKey
import com.jtech.zemer.constants.AudioOffload
import com.jtech.zemer.constants.AudioQuality
import com.jtech.zemer.constants.AudioQualityKey
import com.jtech.zemer.constants.AutoDownloadOnLikeKey
import com.jtech.zemer.constants.AutoLoadMoreKey
import com.jtech.zemer.constants.AutoSkipNextOnErrorKey
import com.jtech.zemer.constants.CastEnabledKey
import com.jtech.zemer.constants.DisableLoadMoreWhenRepeatAllKey
import com.jtech.zemer.constants.HistoryDuration
import com.jtech.zemer.constants.PersistentQueueKey
import com.jtech.zemer.constants.ParticlesEnabledKey
import com.jtech.zemer.constants.ReplayGainMode
import com.jtech.zemer.constants.ReplayGainModeKey
import com.jtech.zemer.constants.ReplayGainPreampKey
import com.jtech.zemer.constants.CrossfadeDurationKey
import com.jtech.zemer.constants.SeekExtraSeconds
import com.jtech.zemer.constants.ShowWaveformKey
import com.jtech.zemer.constants.SkipSilenceKey
import com.jtech.zemer.constants.StopMusicOnTaskClearKey
import com.jtech.zemer.ui.component.ActionPromptDialog
import com.jtech.zemer.ui.component.EnumListPreference
import com.jtech.zemer.ui.component.IconButton
import com.jtech.zemer.ui.component.PreferenceEntry
import com.jtech.zemer.ui.component.PreferenceGroupTitle
import com.jtech.zemer.ui.component.SliderPreference
import com.jtech.zemer.ui.component.SwitchPreference
import com.jtech.zemer.ui.player.CastDownloadDialog
import com.jtech.zemer.ui.utils.backToMain
import com.jtech.zemer.utils.rememberEnumPreference
import com.jtech.zemer.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val (audioQuality, onAudioQualityChange) = rememberEnumPreference(
        AudioQualityKey,
        defaultValue = AudioQuality.AUTO
    )
    val (persistentQueue, onPersistentQueueChange) = rememberPreference(
        PersistentQueueKey,
        defaultValue = true
    )
    val (skipSilence, onSkipSilenceChange) = rememberPreference(
        SkipSilenceKey,
        defaultValue = true
    )
    val (audioNormalization, onAudioNormalizationChange) = rememberPreference(
        AudioNormalizationKey,
        defaultValue = true
    )
    val (castEnabled, onCastEnabledChange) = rememberPreference(
        CastEnabledKey,
        defaultValue = false
    )
    val playerConnection = LocalPlayerConnection.current
    var showCastDownloadDialog by remember { mutableStateOf(false) }

    val (audioOffload, onAudioOffloadChange) = rememberPreference(
        key = AudioOffload,
        defaultValue = false
    )

    val (seekExtraSeconds, onSeekExtraSeconds) = rememberPreference(
        SeekExtraSeconds,
        defaultValue = false
    )

    val (showWaveform, onShowWaveformChange) = rememberPreference(
        ShowWaveformKey,
        defaultValue = false
    )
    val (replayGainMode, onReplayGainModeChange) = rememberEnumPreference(
        ReplayGainModeKey,
        defaultValue = ReplayGainMode.AUTO,
    )
    val (replayGainPreamp, onReplayGainPreampChange) = rememberPreference(
        ReplayGainPreampKey,
        defaultValue = 0f,
    )
    var showReplayGainPreampDialog by remember { mutableStateOf(false) }
    val (showParticles, onShowParticlesChange) = rememberPreference(
        ParticlesEnabledKey,
        defaultValue = false,
    )
    val (crossfadeDuration, onCrossfadeDurationChange) = rememberPreference(
        CrossfadeDurationKey,
        defaultValue = 0,
    )
    var showCrossfadeDialog by remember { mutableStateOf(false) }

    val (autoLoadMore, onAutoLoadMoreChange) = rememberPreference(
        AutoLoadMoreKey,
        defaultValue = true
    )
    val (disableLoadMoreWhenRepeatAll, onDisableLoadMoreWhenRepeatAllChange) = rememberPreference(
        DisableLoadMoreWhenRepeatAllKey,
        defaultValue = false
    )
    val (autoDownloadOnLike, onAutoDownloadOnLikeChange) = rememberPreference(
        AutoDownloadOnLikeKey,
        defaultValue = false
    )
    val (autoSkipNextOnError, onAutoSkipNextOnErrorChange) = rememberPreference(
        AutoSkipNextOnErrorKey,
        defaultValue = false
    )
    val (stopMusicOnTaskClear, onStopMusicOnTaskClearChange) = rememberPreference(
        StopMusicOnTaskClearKey,
        defaultValue = false
    )
    val (historyDuration, onHistoryDurationChange) = rememberPreference(
        HistoryDuration,
        defaultValue = 30f
    )

    val backFocus = remember { FocusRequester() }
    val firstFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        firstFocus.requestFocus()
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState()),
    ) {

        PreferenceGroupTitle(
            title = stringResource(R.string.player)
        )

        EnumListPreference(
            title = { Text(stringResource(R.string.audio_quality)) },
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            selectedValue = audioQuality,
            onValueSelected = onAudioQualityChange,
            valueText = {
                when (it) {
                    AudioQuality.AUTO -> stringResource(R.string.audio_quality_auto)
                    AudioQuality.HIGH -> stringResource(R.string.audio_quality_high)
                    AudioQuality.LOW -> stringResource(R.string.audio_quality_low)
                }
            },
            modifier = Modifier.focusRequester(firstFocus),
        )

        SliderPreference(
            title = { Text(stringResource(R.string.history_duration)) },
            icon = { Icon(painterResource(R.drawable.history), null) },
            value = historyDuration,
            onValueChange = onHistoryDurationChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.skip_silence)) },
            icon = { Icon(painterResource(R.drawable.fast_forward), null) },
            checked = skipSilence,
            onCheckedChange = onSkipSilenceChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.audio_normalization)) },
            icon = { Icon(painterResource(R.drawable.volume_up), null) },
            checked = audioNormalization,
            onCheckedChange = onAudioNormalizationChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.audio_offload)) },
            description = stringResource(R.string.audio_offload_description),
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            checked = audioOffload,
            onCheckedChange = onAudioOffloadChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.waveform)) },
            description = stringResource(R.string.waveform_desc),
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            checked = showWaveform,
            onCheckedChange = onShowWaveformChange,
        )

        PreferenceEntry(
            title = { Text(stringResource(R.string.equalizer)) },
            description = stringResource(R.string.equalizer_desc),
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            onClick = { navController.navigate("settings/equalizer") },
        )

        EnumListPreference(
            title = { Text(stringResource(R.string.replay_gain)) },
            description = stringResource(R.string.replay_gain_desc),
            icon = { Icon(painterResource(R.drawable.volume_up), null) },
            selectedValue = replayGainMode,
            onValueSelected = onReplayGainModeChange,
            valueText = {
                when (it) {
                    ReplayGainMode.OFF -> stringResource(R.string.replay_gain_off)
                    ReplayGainMode.AUTO -> stringResource(R.string.replay_gain_auto)
                    ReplayGainMode.FORCE -> stringResource(R.string.replay_gain_force)
                }
            },
        )

        PreferenceEntry(
            title = { Text(stringResource(R.string.replay_gain_preamp)) },
            description = "%.1f dB".format(replayGainPreamp),
            icon = { Icon(painterResource(R.drawable.volume_up), null) },
            onClick = { showReplayGainPreampDialog = true },
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.particles)) },
            description = stringResource(R.string.particles_desc),
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            checked = showParticles,
            onCheckedChange = onShowParticlesChange,
        )

        PreferenceEntry(
            title = { Text(stringResource(R.string.crossfade)) },
            description = stringResource(R.string.crossfade_desc),
            icon = { Icon(painterResource(R.drawable.graphic_eq), null) },
            onClick = { showCrossfadeDialog = true },
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.seek_seconds_addup)) },
            description = stringResource(R.string.seek_seconds_addup_description),
            icon = { Icon(painterResource(R.drawable.arrow_forward), null) },
            checked = seekExtraSeconds,
            onCheckedChange = onSeekExtraSeconds,
        )

        PreferenceGroupTitle(
            title = stringResource(R.string.queue)
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.persistent_queue)) },
            description = stringResource(R.string.persistent_queue_desc),
            icon = { Icon(painterResource(R.drawable.queue_music), null) },
            checked = persistentQueue,
            onCheckedChange = onPersistentQueueChange
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.auto_load_more)) },
            description = stringResource(R.string.auto_load_more_desc),
            icon = { Icon(painterResource(R.drawable.playlist_add), null) },
            checked = autoLoadMore,
            onCheckedChange = onAutoLoadMoreChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.disable_load_more_when_repeat_all)) },
            description = stringResource(R.string.disable_load_more_when_repeat_all_desc),
            icon = { Icon(painterResource(R.drawable.repeat), null) },
            checked = disableLoadMoreWhenRepeatAll,
            onCheckedChange = onDisableLoadMoreWhenRepeatAllChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.auto_download_on_like)) },
            description = stringResource(R.string.auto_download_on_like_desc),
            icon = { Icon(painterResource(R.drawable.download), null) },
            checked = autoDownloadOnLike,
            onCheckedChange = onAutoDownloadOnLikeChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.auto_skip_next_on_error)) },
            description = stringResource(R.string.auto_skip_next_on_error_desc),
            icon = { Icon(painterResource(R.drawable.skip_next), null) },
            checked = autoSkipNextOnError,
            onCheckedChange = onAutoSkipNextOnErrorChange,
        )

        PreferenceGroupTitle(
            title = stringResource(R.string.cast)
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.enable_casting)) },
            description = stringResource(R.string.enable_casting_description),
            icon = { Icon(painterResource(R.drawable.cast), null) },
            checked = castEnabled,
            onCheckedChange = { enabled ->
                onCastEnabledChange(enabled)
                // Prompt to download the cast support lib right away when enabling (it isn't bundled).
                if (enabled && playerConnection?.service?.castLibLoader?.isReady != true) {
                    showCastDownloadDialog = true
                }
            },
        )

        PreferenceGroupTitle(
            title = stringResource(R.string.misc)
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.stop_music_on_task_clear)) },
            icon = { Icon(painterResource(R.drawable.clear_all), null) },
            checked = stopMusicOnTaskClear,
            onCheckedChange = onStopMusicOnTaskClearChange,
        )
    }

    if (showCastDownloadDialog) {
        playerConnection?.let { pc ->
            CastDownloadDialog(playerConnection = pc, onDismiss = { showCastDownloadDialog = false })
        }
    }

    if (showReplayGainPreampDialog) {
        var preampValue by remember { mutableFloatStateOf(replayGainPreamp) }
        ActionPromptDialog(
            titleBar = {
                Text(
                    text = stringResource(R.string.replay_gain_preamp),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            onDismiss = { showReplayGainPreampDialog = false },
            onConfirm = {
                showReplayGainPreampDialog = false
                onReplayGainPreampChange(preampValue)
            },
            onCancel = { showReplayGainPreampDialog = false },
            onReset = { preampValue = 0f },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "%.1f dB".format(preampValue),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Slider(
                        value = preampValue,
                        onValueChange = { preampValue = it },
                        valueRange = -12f..12f,
                        steps = 48,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        )
    }

    if (showCrossfadeDialog) {
        var crossfadeValue by remember { mutableFloatStateOf(crossfadeDuration.toFloat()) }
        ActionPromptDialog(
            titleBar = {
                Text(
                    text = stringResource(R.string.crossfade),
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            onDismiss = { showCrossfadeDialog = false },
            onConfirm = {
                showCrossfadeDialog = false
                onCrossfadeDurationChange(crossfadeValue.toInt())
            },
            onCancel = { showCrossfadeDialog = false },
            onReset = { crossfadeValue = 0f },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(
                            R.string.seconds_format,
                            crossfadeValue.toInt(),
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Slider(
                        value = crossfadeValue,
                        onValueChange = { crossfadeValue = it },
                        valueRange = 0f..12f,
                        steps = 11,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        )
    }
    TopAppBar(
        title = { Text(stringResource(R.string.player_and_audio)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier
                        .focusRequester(backFocus)
                        .focusProperties { down = firstFocus }
                )
            }
        }
    )
}
