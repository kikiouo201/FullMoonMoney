package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.custom.TableContentCell
import com.example.fullmoonmoney.ui.custom.TableHeaderCell
import com.example.fullmoonmoney.ui.datePicker.MonthDropdownMenu
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

// 月記帳
@Composable
fun MonthlyAccounting(viewModel: MonthlyAccountingViewModel = viewModel()) {
    val netWorth by remember { viewModel.netWorth }
    val selectedDate by remember { viewModel.selectedDate }
    val monthlyCategory by remember { viewModel.currentMonthlyCategory }
    var isAddFixedItemDialog by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "淨值 : $netWorth")
            MonthDropdownMenu(selectedDate) {
                viewModel.setCurrentStatus(monthlyCategory, it)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            viewModel.monthlyCategories.forEach {
                Text(text = stringResource(id = it.categoryName),
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setCurrentStatus(it, selectedDate) }
                        .background(
                            if (it == monthlyCategory) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                        .padding(10.dp)
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .wrapContentHeight(align = Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            text = "圖表",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { isAddFixedItemDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "create"
                )
            }
        }
        MonthlyAccountingTable(viewModel)
        if (isAddFixedItemDialog) {
            AddFixedItemDialog(
                itemData = viewModel.getItemData(),
                onAdd = {
                    viewModel.setItemData(it)
                    isAddFixedItemDialog = false
                },
                onCancel = { isAddFixedItemDialog = false }
            )
        }
    }
}

@Composable
fun MonthlyAccountingTable(viewModel: MonthlyAccountingViewModel) {
    val titleData = listOf(R.string.amount, R.string.item)
    val tableData by remember { viewModel.selectedTableData }
    var isAddItemDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        TableHeaderCell(
            textList = titleData,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        tableData.forEachIndexed { index, data ->
            var mData = remember { mutableStateOf(data) }
            mData.value = data
            Row(Modifier.fillMaxWidth()) {
                TableContentCell(
                    text = mData.value.first,
                    textFieldText = mData.value.second
                ) { str ->
                    mData.value = Pair(mData.value.first, str)
                    mutableListOf<Pair<String, String>>().let {
                        it.addAll(tableData)
                        it[index] = Pair(tableData[index].first, str)
                        viewModel.setCurrentTableData(data = it)
                    }
                }
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { isAddItemDialog = true }
        ) {
            Text(text = "+")
        }
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "總共 : ",
                Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
            Text(
                text = viewModel.getTotal().toString(),
                Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
        }
        if (isAddItemDialog) {
            AddItemDialog(
                onAdd = { text ->
                    if (text.isEmpty()) return@AddItemDialog
                    mutableListOf<Pair<String, String>>().let { list ->
                        list.addAll(tableData)
                        list.add(Pair(text, ""))
                        viewModel.setCurrentTableData(data = list)
                    }
                    isAddItemDialog = false
                },
                onCancel = { isAddItemDialog = false }
            )
        }
    }
}

// 增加項目的 dialog
@Composable
fun AddItemDialog(onAdd: (String) -> Unit, onCancel: () -> Unit) {
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(text = stringResource(R.string.add_item)) },
        text = {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${stringResource(R.string.item)} : ")
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.padding(5.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(value) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

// 增加固定項目的 dialog
@Composable
fun AddFixedItemDialog(
    itemData: List<String>,
    onAdd: (String) -> Unit,
    onCancel: () -> Unit
) {
    var value by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text(text = stringResource(R.string.add_item)) },
        text = {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "${stringResource(R.string.item)} : ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                itemData.forEach {
                    Text(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        text = it,
                        textAlign = TextAlign.Center
                    )
                }
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
                        value = value,
                        onValueChange = { value = it },
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(value) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MonthlyAccountingPreview() {
    FullMoonMoneyTheme {
        MonthlyAccounting()
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemDataDialogPreview() {
    FullMoonMoneyTheme {
        val itemData = listOf("A 銀行", "B 銀行", "現金")
        AddFixedItemDialog(itemData = itemData, onAdd = {}) {}
    }
}