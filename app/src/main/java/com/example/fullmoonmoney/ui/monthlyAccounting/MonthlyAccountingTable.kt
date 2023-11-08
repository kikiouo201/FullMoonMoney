package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.AssetDetail
import com.example.fullmoonmoney.ui.custom.TableContentCell
import com.example.fullmoonmoney.ui.custom.TableHeaderCell

@Composable
fun MonthlyAccountingTable(
    total: String,
    detailList: List<AssetDetail>,
    isEditContent: Boolean,
    setAssetDetailItem: (String) -> Unit,
    setAssetDetail: (Int, String) -> Unit,
) {
    val titleData = listOf(R.string.item, R.string.amount)
    var amount by remember { mutableStateOf("") }
    var isAddItemDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        TableHeaderCell(
            textList = titleData,
            color = MaterialTheme.colorScheme.primary
        )
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
                text = total,
                Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
        }
        detailList.forEachIndexed { index, detail ->
            amount = detail.amount

            Row(Modifier.fillMaxWidth()) {
                TableContentCell(
                    text = detail.item,
                    textFieldText = amount,
                    isEdit = isEditContent
                ) {
                    amount = it
                    setAssetDetail(index, it)
                }
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { isAddItemDialog = true }
        ) {
            Text(text = "+")
        }
        if (isAddItemDialog) {
            AddItemDialog(
                onAdd = { text ->
                    if (text.isEmpty()) return@AddItemDialog
                    setAssetDetailItem(text)
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
