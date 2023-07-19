package com.example.fullmoonmoney.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.monthlyAccounting.MonthlyAccounting

// 首頁
@Composable
fun Home(
    homeViewModel: HomeViewModel = viewModel()
) {
    var state by remember { homeViewModel.state }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(20.dp),
                text = "6月",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(80.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = "圖表",
                style = MaterialTheme.typography.bodyLarge
            )
            TabRow(
                selectedTabIndex = state,
            ) {
                Tab(
                    text = { Text(stringResource(R.string.monthly_accounting)) },
                    selected = state == 0,
                    onClick = { state = 0 }
                )
                Tab(
                    text = { Text(stringResource(R.string.general_accounting)) },
                    selected = state == 1,
                    onClick = { state = 1 }
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "選擇 tab ${state + 1}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (state == 0) {
                MonthlyAccounting()
            } else {
                GeneralAccounting(
                    text = stringResource(R.string.general_accounting),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// 一般記帳
@Composable
fun GeneralAccounting(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FullMoonMoneyTheme {
        Home()
    }
}