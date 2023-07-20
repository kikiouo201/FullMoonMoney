package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.custom.TableContentCell
import com.example.fullmoonmoney.ui.custom.TableHeaderCell

// 月記帳
@Composable
fun MonthlyAccounting(
    viewModel: MonthlyAccountingViewModel = viewModel()
) {
    var tableData by remember { viewModel.tableData }
    var isAddItemDialog by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier.padding(15.dp)
    ) {
        var titleData = listOf(R.string.amount, R.string.item)
        item {
            TableHeaderCell(
                textList = titleData,
                color = Color(android.graphics.Color.parseColor("#CCCCFF"))
            )
        }
        items(tableData) {
            val (title, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableContentCell(text = title, textFieldText = text.toString())
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    isAddItemDialog = true
                }) {
                    Text(text = "+")
                }
            }
        }
        item {
            if (isAddItemDialog) {
                AddItemDialog { str ->
                    mutableListOf<Pair<String, Int>>().let {
                        it.addAll(tableData)
                        it.add(Pair(str, 0))
                        viewModel.tableData.value = it
                    }
                    isAddItemDialog = false
                }
            }
        }
    }
}

// 增加項目的 dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(onAdd: (String) -> Unit) {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    AlertDialog(
        onDismissRequest = {},
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
        }
    )
}
