package com.jtech.zemer.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.jtech.zemer.ui.theme.AppColors

@Composable
fun LoadingScreen(
    isVisible: Boolean,
    value: Int,
) {
    if (isVisible) {
        Dialog (
            onDismissRequest = {}
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Progress $value%",
                    color = AppColors.onMedia,
                    style = MaterialTheme.typography.headlineSmall,
                )

            }
        }
    }
}
