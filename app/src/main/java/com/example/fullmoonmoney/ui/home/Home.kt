package com.example.fullmoonmoney.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.monthlyAccounting.MonthlyAccounting
import com.example.fullmoonmoney.ui.monthlyAccounting.MonthlyAccountingViewModel
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

// 首頁
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    homeViewModel: HomeViewModel = viewModel()
) {
    var state by remember { homeViewModel.state }
    var monthlyCategory by remember { homeViewModel.monthlyCategory }
    var selectedMonth by remember { mutableStateOf(1) }
    val monthlyAccountingViewModel = MonthlyAccountingViewModel()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("My App")
        })
    }, bottomBar = {
        BottomAppBar(Modifier.fillMaxWidth()) {
            homeViewModel.homeCategories.forEachIndexed { index, category ->
                NavigationBarItem(selected = state == index, onClick = { state = index }, icon = {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = stringResource(category.categoryName)
                    )
                }, label = {
                    Text(stringResource(category.categoryName))
                })
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (homeViewModel.homeCategories[state]) {
                HomeCategory.Monthly -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "淨值 : ${monthlyAccountingViewModel.netWorth.value}")
                        MonthDropdownMenu(selectedMonth) { selectedMonth = it }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        monthlyAccountingViewModel.monthlyCategories.forEach {
                            Button(onClick = { monthlyCategory = it }) {
                                Text(text = stringResource(id = it.categoryName))
                            }
                        }
                    }
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(80.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically),
                        text = "圖表",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    monthlyAccountingViewModel.setCurrentStatus(monthlyCategory, selectedMonth)
                    MonthlyAccounting(monthlyAccountingViewModel)
                }

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
        modifier = modifier, text = text, style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun MonthDropdownMenu(selectedMonth: Int, onSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(modifier = Modifier.width(70.dp),
            onClick = { expanded = true }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$selectedMonth 月")
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier.fillMaxHeight(),
                    contentDescription = ""
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            repeat(12) {
                DropdownMenuItem(text = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "${it + 1} 月",
                        modifier = Modifier.fillMaxWidth()
                    )
                }, onClick = {
                    onSelected(it + 1)
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FullMoonMoneyTheme {
        Home()
    }
}