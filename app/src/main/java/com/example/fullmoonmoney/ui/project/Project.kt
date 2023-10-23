package com.example.fullmoonmoney.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun Project(viewModel: ProjectViewModel = viewModel()) {
}

@Preview(showBackground = true)
@Composable
fun ProjectPreview() {
    FullMoonMoneyTheme {
        Project()
    }
}