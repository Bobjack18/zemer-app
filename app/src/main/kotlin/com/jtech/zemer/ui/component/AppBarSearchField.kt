package com.jtech.zemer.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.jtech.zemer.R

/**
 * The shared in-app-bar search field used while a screen is in its "searching" state: a borderless,
 * transparent [TextField] sized like the app-bar title. Reused by the library/playlist/history
 * screens so the inline search input looks and behaves identically everywhere.
 */
@Composable
fun AppBarSearchField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.search),
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleLarge,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
    )
}
