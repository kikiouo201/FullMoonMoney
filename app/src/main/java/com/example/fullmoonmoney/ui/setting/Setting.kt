package com.example.fullmoonmoney.ui.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.fullmoonmoney.R

@Composable
fun Setting() {
    // TODO 每個月的收入分配
    // TODO 支出預算
    Text(
        text = stringResource(R.string.setting),
        modifier = Modifier.fillMaxSize(),
        style = MaterialTheme.typography.bodyLarge
    )
}