package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.custom.MonthlyAccountingBarChart
import com.example.fullmoonmoney.ui.custom.ProgressIndicator
import com.example.fullmoonmoney.ui.custom.getProgress
import com.example.fullmoonmoney.ui.dataFormat.formatCurrency
import com.example.fullmoonmoney.ui.datePicker.MonthDropdownMenu
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

// 月記帳
@Composable
fun MonthlyAccounting(viewModel: MonthlyAccountingViewModel = viewModel()) {

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val targetPrice by remember { viewModel.targetPrice }
    val currentMoney by remember { viewModel.currentMoney }
    val monthTargetPrice by remember { viewModel.monthTargetPrice }
    val detailList = viewState.detailList.toMutableList()

    var isEditContent by remember { mutableStateOf(false) }
    var isAddFixedItemDialog by remember { mutableStateOf(false) }
    var isSave = false

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "淨值 : ${formatCurrency(viewState.netWorth)}")
            MonthDropdownMenu(viewState.date) {
                viewModel.setSelectedDate(viewState.date)
            }
        }
        ProgressIndicator(
            progressTitle = "淨值 :",
            targetTitle = ": 目標",
            progressText = formatCurrency(viewState.netWorth),
            targetText = formatCurrency(targetPrice),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            progress = getProgress(viewState.netWorth, targetPrice)
        )
        ProgressIndicator(
            progressTitle = "目前 :",
            targetTitle = ": 需存",
            progressText = formatCurrency(currentMoney),
            targetText = formatCurrency(monthTargetPrice),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            progress = getProgress(currentMoney, monthTargetPrice),
            progressColor = colorResource(R.color.teal_700),
            backgroundColor = colorResource(R.color.orange_200),
        )
        MonthlyAccountingBarChart(
            Modifier.height(100.dp),
            MaterialTheme.colorScheme.onPrimary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            viewModel.categories.forEach {
                Text(
                    text = stringResource(id = it.categoryName),
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            if (isEditContent && isSave) viewModel.setAssetDetailList(detailList)
                            isEditContent = false
                            isSave = false
                            viewModel.setSelectedCategory(it)
                        }
                        .background(
                            if (it == viewState.assetCategory)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(10.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = {
                if (isEditContent && isSave) {
                    viewModel.setAssetDetailList(detailList)
                    isSave = false
                }
                isEditContent = !isEditContent
            }) {
                if (isEditContent)
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done"
                    )
                else
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit"
                    )
            }
            IconButton(onClick = { isAddFixedItemDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
        MonthlyAccountingTable(
            total = viewModel.getTotal().toString(),
            detailList = viewState.detailList,
            isEditContent = isEditContent,
            setAssetDetailItem = { viewModel.setAssetDetail(it, "") }
        ) { index, amount ->
            isSave = true
            detailList[index].amount = amount
        }
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

// 增加固定項目的 dialog
@Composable
fun AddFixedItemDialog(
    itemData: List<String>,
    onAdd: (String) -> Unit,
    onCancel: () -> Unit,
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