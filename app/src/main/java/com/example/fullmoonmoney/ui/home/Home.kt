package com.example.fullmoonmoney.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.fullmoonmoney.ui.generalAccounting.GeneralAccounting
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
            BottomAppBar(Modifier.fillMaxWidth()) {
                homeViewModel.homeCategories.forEachIndexed { index, category ->
                    NavigationBarItem(
                        selected = state == index,
                        onClick = { state = index },
                        icon = {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = stringResource(category.categoryName)
                            )
                        },
                        label = {
                            Text(stringResource(category.categoryName))
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (homeViewModel.homeCategories[state]) {
                HomeCategory.Monthly -> MonthlyAccounting()
                HomeCategory.General -> GeneralAccounting()

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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FullMoonMoneyTheme {
        Home()
    }
}