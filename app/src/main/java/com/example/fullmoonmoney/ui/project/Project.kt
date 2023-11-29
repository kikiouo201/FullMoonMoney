package com.example.fullmoonmoney.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.ui.dataFormat.formatCurrency
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun Project(viewModel: ProjectViewModel = viewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = "收入")
                Text(text = formatCurrency(36000))
            }
            Column(Modifier.weight(1f)) {
                Text(text = "預計花費")
                Text(text = formatCurrency(16000))
            }
            Column(Modifier.weight(1f)) {
                Text(text = "存")
                Text(text = formatCurrency(2000))
            }
            Column(Modifier.weight(1f)) {
                Text(text = "剩下")
                Text(text = formatCurrency(6000))
            }
        }
        Text(text = "選定儲蓄：")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "結婚費用",
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            )
            Text(text = " : " + formatCurrency(2000))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "家庭緊急預備金",
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            )
            Text(text = " : " + formatCurrency(2000))
        }
        Text(text = "選定預算：")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "計畫 a",
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
            )
            Text(text = " : " + formatCurrency(16000))
        }
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(max = 700.dp)
                .padding(vertical = 5.dp),
            columns = GridCells.Fixed(2)
        ) {
            viewModel.projectList.forEach {
                item {
                    Text(
                        text = stringResource(id = it.categoryName),
                        color = Color.White,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.setSelectedProject(it) }
                            .background(
                                if (it == viewState.projectCategory) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                            .padding(10.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        when (viewState.projectCategory) {
            ProjectCategory.FixedIncomeAndExpenditure -> FixedIncomeAndExpenditure(viewState.fixedItemList)
            ProjectCategory.LivingExpensesBudget -> LivingExpensesBudget(viewState.budgetItemList)
            ProjectCategory.TargetSavings -> TargetSavings()
            ProjectCategory.Invest -> Invest()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectPreview() {
    FullMoonMoneyTheme {
        Project()
    }
}