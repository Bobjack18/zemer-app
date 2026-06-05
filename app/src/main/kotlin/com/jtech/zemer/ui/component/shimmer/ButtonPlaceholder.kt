package com.jtech.zemer.ui.component.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.jtech.zemer.ui.theme.PillShape

@Composable
fun ButtonPlaceholder(modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .height(ButtonDefaults.MinHeight)
            .clip(PillShape)
            .background(MaterialTheme.colorScheme.onSurface),
    )
}
