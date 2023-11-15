package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.AccountingDetail
import com.example.fullmoonmoney.data.Project
import com.example.fullmoonmoney.ui.custom.MonthBarChart
import com.example.fullmoonmoney.ui.datePicker.MonthDropdownMenu
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun GeneralAccounting(viewModel: GeneralAccountingViewModel = viewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    var isAddFixedItemDialog by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "總共 : ${viewModel.getTotal()}")
            MonthDropdownMenu(viewState.date) {
                viewModel.setCurrentStatus(it)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { viewModel.setSelectedAccountingCategory(true) }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "star",
                    tint = if (viewState.isCategory) colorResource(R.color.yellow_500) else MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { viewModel.setSelectedAccountingCategory(false) }) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "list",
                    tint = if (!viewState.isCategory) colorResource(R.color.yellow_500) else MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { isAddFixedItemDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Create, contentDescription = "create"
                )
            }
        }
        if (viewState.isCategory && viewState.selectedProject != null) {
            ProjectListAccounting(viewState.selectedProject!!, viewState.projectList) {
                viewModel.setSelectedProject(it)
            }
        }

        DetailListAccounting(viewState.detailList)

        if (isAddFixedItemDialog && viewState.isCategory) {
            AddCategoryDialog(onAdd = { item ->
                isAddFixedItemDialog = false
                viewModel.setProject(item)
            }, onCancel = { isAddFixedItemDialog = false })
        } else if (isAddFixedItemDialog) {
            AddItemDialog(onAdd = { item, price, project ->
                viewModel.setAccountingDetailDao(
                    AccountingDetail(
                        item = item,
                        amount = price.toIntOrNull() ?: 0,
                        date = viewModel.getSelectedDataKey(),
                        projectList = project
                    )
                )
                isAddFixedItemDialog = false
            }, onCancel = { isAddFixedItemDialog = false })
        }
    }
}

@Composable
fun ProjectListAccounting(
    selectedProject: Project,
    projectList: List<Project>,
    setSelectedProject: (Project) -> Unit,
) {
    Column(Modifier.padding(5.dp)) {
        Text(
            text = selectedProject.name,
            color = Color.White,
            modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.category),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge
        )
        MonthBarChart(
            Modifier.height(100.dp), MaterialTheme.colorScheme.onPrimary, listOf(
                listOf(3000f, 2000f),
                listOf(5000f, 2000f),
                listOf(4000f, 3000f),
                listOf(5000f, 4000f),
                listOf(5000f, 3000f),
                listOf(5000f, 4000f),
                listOf(5000f, 2000f),
                listOf(4000f, 4000f),
                listOf(6000f, 2000f),
                listOf(4000f, 3000f),
                listOf(5000f, 2000f),
                listOf(5000f, 1000f)
            )
        )
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(max = 700.dp)
                .padding(vertical = 5.dp),
            columns = GridCells.Fixed(3)
        ) {
            projectList.forEach {
                item {
                    Text(
                        text = it.name,
                        color = Color.White,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { setSelectedProject(it) }
                            .background(
                                if (it.name == selectedProject.name) MaterialTheme.colorScheme.primary
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
    }
}

@Composable
fun DetailListAccounting(detailList: List<AccountingDetail>?) {
    detailList?.forEach { detail ->
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = detail.item)
                Text(text = "$${detail.amount}")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${stringResource(id = R.string.project)} : ")
                detail.projectList.forEachIndexed { index, project ->
                    Text(
                        text = project, modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(1.dp)
                    )
                    if (index < detail.projectList.size - 1) Text(text = "、")
                }
            }
        }
    }
}

// 增加分類的 dialog
@Composable
fun AddCategoryDialog(
    onAdd: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var itemValue by remember { mutableStateOf("") }

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
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(itemValue) }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancel() }) {
                Text(stringResource(R.string.cancel))
            }
        })
}

// 增加固定項目的 dialog
@Composable
fun AddItemDialog(
    onAdd: (String, String, MutableList<String>) -> Unit,
    onCancel: () -> Unit,
) {
    var itemValue by remember { mutableStateOf("") }
    var priceValue by remember { mutableStateOf("") }
    var projectValue by remember { mutableStateOf("") }

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
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(itemValue, priceValue, mutableListOf(projectValue)) }) {
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
fun GeneralAccountingPreview() {
    FullMoonMoneyTheme {
        GeneralAccounting()
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryAccountingPreview() {
    FullMoonMoneyTheme {
        ProjectListAccounting(
            selectedProject = Project(name = "早餐"), projectList = listOf(
                Project(name = "早餐"), Project(name = "午餐"), Project(name = "晚餐")
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun AccountingItemPreview() {
    FullMoonMoneyTheme {
        DetailListAccounting(
            listOf(
                AccountingDetail(
                    item = "麥當勞",
                    amount = 120,
                    date = "2023/1",
                    projectList = mutableListOf("午餐", "晚餐")
                )
            )
        )
    }
}