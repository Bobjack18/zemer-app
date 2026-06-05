package com.jtech.zemer.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.jtech.zemer.ui.theme.Dimens

/**
 * Settings row that shows the current selection and opens a [ListDialog] to pick one of [values].
 * The canonical "pick one of N" preference (see docs/ui/standards.md) - do not hand-roll a radio
 * list. Rows are clickable (focusable for D-pad), so the picker is D-pad operable.
 */
@Composable
fun <T> SelectPreference(
    title: @Composable () -> Unit,
    selectedValue: T,
    values: List<T>,
    valueText: @Composable (T) -> String,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    isEnabled: Boolean = true,
) {
    var showDialog by remember { mutableStateOf(false) }

    PreferenceEntry(
        modifier = modifier,
        title = title,
        description = valueText(selectedValue),
        icon = icon,
        onClick = { showDialog = true },
        isEnabled = isEnabled,
    )

    if (showDialog) {
        ListDialog(onDismiss = { showDialog = false }) {
            items(values) { value ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            onValueSelected(value)
                            showDialog = false
                        }
                        .padding(horizontal = Dimens.space4, vertical = Dimens.space3),
                ) {
                    RadioButton(selected = value == selectedValue, onClick = null)
                    Spacer(Modifier.width(Dimens.space4))
                    Text(text = valueText(value), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
