package com.example.fullmoonmoney.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.custom.EditContentCell
import com.example.fullmoonmoney.ui.dataFormat.formatCurrency
import com.example.fullmoonmoney.ui.dataFormat.formatDecimal
import com.example.fullmoonmoney.ui.dataFormat.formatPercentage
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun LivingExpensesBudget(budgetItemList: List<BudgetItem>) {
    val budget by remember { mutableStateOf(30000) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text(text = stringResource(id = R.string.living_expenses_budget))
        Text(text = "每年：${formatCurrency(budget * 4 * 12)}")
        Text(text = "每月：${formatCurrency(budget * 4)}")
        Text(text = "每週：${formatCurrency(budget)}")
        Text(text = "每日：${formatDecimal(budget.toDouble() / 7)}")
        BudgetItem(
            name = "伙食費",
            total = 15000,
            percentage = formatPercentage(15000, budget),
            budgetItemList = budgetItemList
        )
        BudgetItem(
            name = "娛樂費",
            total = 10000,
            percentage = formatPercentage(10000, budget),
            budgetItemList = budgetItemList
        )
    }
}

@Composable
fun BudgetItem(name: String, total: Int, percentage: String, budgetItemList: List<BudgetItem>) {
    var isDetail by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable { isDetail = !isDetail }
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.Create,
                    contentDescription = "create"
                )
                Text(modifier = Modifier.padding(5.dp), text = name)
                Text(modifier = Modifier.padding(5.dp), text = percentage)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.padding(5.dp), text = formatCurrency(total))
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = if (isDetail) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                    contentDescription = "ArrowDown"
                )
            }
        }
        if (isDetail) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "項目",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(2f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(5.dp),
                        )
                        Text(
                            text = "價錢",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(2f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(5.dp),
                        )
                        Text(
                            text = "次數",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(5.dp),
                        )
                    }
                    budgetItemList.forEach {
                        EditContentCell(it.item, it.price, it.frequency, {}, {}) {}
                    }
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(30.dp),
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "AddCircle",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun LivingExpensesBudgetPreview() {
    FullMoonMoneyTheme {
        LivingExpensesBudget(
            budgetItemList = listOf(
                BudgetItem(item = "食物", price = 30000, frequency = 1),
                BudgetItem(item = "交通", price = 20000, frequency = 2)
            )
        )
    }
}