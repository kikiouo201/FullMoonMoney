package com.example.fullmoonmoney.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.monthlyAccounting.MonthlyAccounting
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

// 首頁
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel = viewModel()
) {
    var state by remember { homeViewModel.state }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("My App")
            })
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                homeViewModel.homeCategories.forEachIndexed { index, category ->
                    NavigationBarItem(selected = state == index,
                        onClick = { state = index },
                        icon = {
                            Icon(
                                when (category) {
                                    HomeCategory.General -> Icons.Outlined.List
                                    HomeCategory.Monthly -> Icons.Outlined.DateRange
                                    HomeCategory.Assets -> Icons.Outlined.ShoppingCart
                                    HomeCategory.Project -> Icons.Outlined.Star
                                }, contentDescription = null
                            )
                        },
                        label =
                        {
                            Text(
                                stringResource(
                                    when (category) {
                                        HomeCategory.General -> R.string.general_accounting
                                        HomeCategory.Monthly -> R.string.monthly_accounting
                                        HomeCategory.Assets -> R.string.assets
                                        HomeCategory.Project -> R.string.project
                                    }
                                )
                            )
                        })
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
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
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "選擇 tab ${state + 1}",
                style = MaterialTheme.typography.bodyLarge
            )
            when (homeViewModel.homeCategories[state]) {
                HomeCategory.Monthly -> MonthlyAccounting()
                HomeCategory.General -> GeneralAccounting(
                    text = stringResource(R.string.general_accounting),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                HomeCategory.Assets -> Text(
                    text = stringResource(R.string.assets),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge
                )

                HomeCategory.Project -> Text(
                    text = stringResource(R.string.project),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge
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