package com.example.fullmoonmoney.ui.project

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun TargetSavings() {
    Text(text = stringResource(id = R.string.target_savings))
}

@Preview(showBackground = true)
@Composable
fun TargetSavingsPreview() {
    FullMoonMoneyTheme {
        TargetSavings()
    }
}