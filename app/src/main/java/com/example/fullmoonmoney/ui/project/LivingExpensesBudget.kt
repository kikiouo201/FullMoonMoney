package com.example.fullmoonmoney.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun LivingExpensesBudget(budgetItemList: List<BudgetItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text(text = stringResource(id = R.string.living_expenses_budget))
        Text(text = "每年：30000")
        Text(text = "每月：30000")
        Text(text = "每週：30000")
        Text(text = "每日：30000")
        BudgetItem(name = "伙食費", budgetItemList)
        BudgetItem(name = "娛樂費", budgetItemList)
    }
}

@Composable
fun BudgetItem(name: String, budgetItemList: List<BudgetItem>) {
    var isDetail by remember { mutableStateOf(true) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.Create,
                    contentDescription = "create"
                )
                Text(modifier = Modifier.padding(5.dp), text = name)
                Text(modifier = Modifier.padding(5.dp), text = "15%")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.padding(5.dp), text = "15,000")
                Icon(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.End)
                        .clickable { isDetail = !isDetail },
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
                            .size(30.dp)
                            .clickable { },
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