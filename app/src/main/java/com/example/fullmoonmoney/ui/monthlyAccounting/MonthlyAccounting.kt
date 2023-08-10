package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.custom.TableContentCell
import com.example.fullmoonmoney.ui.custom.TableHeaderCell
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import java.util.Calendar

// 月記帳
@Composable
fun MonthlyAccounting(viewModel: MonthlyAccountingViewModel = viewModel()) {
    var monthlyCategory by remember { viewModel.currentMonthlyCategory }
    var selectedDate by remember { viewModel.selectedDate }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "淨值 : ${viewModel.netWorth.value}")
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
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (it == monthlyCategory) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    ),
                    onClick = {
                        viewModel.setCurrentStatus(it, selectedDate)
                    }
                ) {
                    Text(
                        text = stringResource(id = it.categoryName),
                        color = Color.White
                    )
                }
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

        MonthlyAccountingTable(viewModel)
    }
}

@Composable
fun MonthlyAccountingTable(viewModel: MonthlyAccountingViewModel) {
    var isAddItemDialog by remember { mutableStateOf(false) }
    val tableData by remember { viewModel.selectedTableData }

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        var titleData = listOf(R.string.amount, R.string.item)

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
            onClick = {
                isAddItemDialog = true
            }
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
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
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
            TextButton(onClick = { onAdd(value.text) }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDropdownMenu(selectedDate: Pair<Int, Int>, onSelected: (Pair<Int, Int>) -> Unit) {
    // TODO 改成只選月日
    var expanded by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = selectedDate.first
    calendar[Calendar.MONTH] = selectedDate.second
    val datePickerState =
        rememberDatePickerState(initialDisplayedMonthMillis = calendar.timeInMillis)

    Box {
        IconButton(modifier = Modifier
            .padding(10.dp)
            .width(80.dp),
            onClick = { expanded = true }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${selectedDate.first}/${selectedDate.second}")
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier.fillMaxHeight(),
                    contentDescription = ""
                )
            }
        }
        if (expanded) {
            DatePickerDialog(
                onDismissRequest = { expanded = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            calendar.timeInMillis = it
                        }
                        onSelected(Pair(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1))
                        expanded = false
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { expanded = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FullMoonMoneyTheme {
        MonthlyAccounting()
    }
}