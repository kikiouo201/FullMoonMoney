package com.example.fullmoonmoney.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.dataFormat.formatCurrency
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun FixedIncomeAndExpenditure(fixedItemList: List<FixedItem>) {
    var isAddItemDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "每個月(收入: ${formatCurrency(45000)} 支出: ${formatCurrency(29000)})")
            IconButton(onClick = { isAddItemDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit"
                )
            }
        }

        fixedItemList.forEach {
            FixedIncomeAndExpenditureItem(it.item, it.price, it.cycle, it.source, it.isExpenditure)
        }
        FixedIncomeAndExpenditureItem("房租", 20000, "每月(3日)", "永豐銀行", false)
        FixedIncomeAndExpenditureItem("訂閱YouTube", 20000, "每月(15日)", "永豐銀行", true)

        if (isAddItemDialog) {
            AddItemDialog(
                onAdd = { item, price, project, cycle -> isAddItemDialog = false },
                onCancel = { isAddItemDialog = false }
            )
        }
    }
}

@Composable
fun FixedIncomeAndExpenditureItem(
    item: String,
    money: Int,
    fixedTime: String,
    source: String,
    isExpenditure: Boolean,
) {
    Column(
        Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item)
            Text(
                text = formatCurrency(money, isExpenditure),
                color = if (isExpenditure) Color.Red else MaterialTheme.colorScheme.onPrimary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = fixedTime, color = colorResource(R.color.grey))
            Text(text = source, color = colorResource(R.color.grey))
        }
    }
}

// 增加固定項目的 dialog
@Composable
fun AddItemDialog(
    onAdd: (String, String, MutableList<String>, String) -> Unit,
    onCancel: () -> Unit,
) {
    var itemValue by remember { mutableStateOf("") }
    var priceValue by remember { mutableStateOf("") }
    var projectValue by remember { mutableStateOf("") }
    var cycleValue by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = { onCancel() },
        title = { Text(text = stringResource(R.string.add_item)) },
        text = {
            Column(modifier = Modifier.padding(5.dp)) {
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.item)} : ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = itemValue,
                        onValueChange = { itemValue = it },
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.price)} : ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = priceValue,
                        onValueChange = { priceValue = it },
                        modifier = Modifier.padding(5.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.project)} : ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = projectValue,
                        onValueChange = { projectValue = it },
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.cycle)} : ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "一次",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { cycleValue = "一次" }
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    )
                    Text(
                        text = "重複",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { cycleValue = "重複" }
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(
                    itemValue,
                    priceValue,
                    mutableListOf(projectValue),
                    cycleValue
                )
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text(stringResource(R.string.cancel))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun FixedIncomeAndExpenditurePreview() {
    FullMoonMoneyTheme {
        FixedIncomeAndExpenditure(
            listOf(
                FixedItem(
                    item = "房租",
                    price = 20000,
                    project = ProjectCategory.FixedIncomeAndExpenditure,
                    cycle = "每月(3日)",
                    source = "永豐銀行",
                    isExpenditure = false
                ),
                FixedItem(
                    item = "訂閱YouTube",
                    price = 20000,
                    project = ProjectCategory.FixedIncomeAndExpenditure,
                    cycle = "每月(15日)",
                    source = "永豐銀行",
                    isExpenditure = true
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemDialogPreview() {
    FullMoonMoneyTheme {
        AddItemDialog(onAdd = { _, _, _, _ -> }) {}
    }
}