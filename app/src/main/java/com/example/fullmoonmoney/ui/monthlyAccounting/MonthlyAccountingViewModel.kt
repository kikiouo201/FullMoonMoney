package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.CategoryDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyAccountingViewModel : ViewModel() {

    private var allTableData = mutableStateOf(mutableStateMapOf<MonthlyCategory, MonthlyData>())
    var currentMonthlyCategory = mutableStateOf(MonthlyCategory.Income)
    var targetPrice = mutableStateOf(30000)
    var netWorth = mutableStateOf(0)
    var currentMoney = mutableStateOf(2000)
    var monthTargetPrice = mutableStateOf(5000)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    var selectedTableData = mutableStateOf(listOf<Pair<String, String>>())
    val monthlyCategories: List<MonthlyCategory> = listOf(
        MonthlyCategory.Income,
        MonthlyCategory.Invest,
        MonthlyCategory.Expenditure,
        MonthlyCategory.Debt
    )

    init {
        // 測試資料
        MonthlyData().let {
            it.data["2023/1"] = listOf(Pair("A 銀行", ""), Pair("B 銀行", ""), Pair("現金", ""))
            it.data["2023/9"] = listOf(Pair("B 銀行", ""), Pair("現金", ""))
            it.fixedItem = mutableListOf("A 銀行", "B 銀行", "現金")
            allTableData.value[MonthlyCategory.Income] = it
        }
        GlobalScope.launch(Dispatchers.IO) {
            Graph.allCategoryDetailsDao.getDetails { monthlyCategory, monthlyDataKey, tableData ->
                tableData?.let {
                    GlobalScope.launch(Dispatchers.IO) {
                        initTableData(MonthlyCategory.valueOf(monthlyCategory), monthlyDataKey, it)
                    }
                }
            }
        }
    }

    fun setCurrentStatus(monthlyCategory: MonthlyCategory, date: Pair<Int, Int>) {
        currentMonthlyCategory.value = monthlyCategory
        selectedDate.value = date
        setMonthlyData(false)
    }

    private suspend fun initTableData(
        monthlyCategory: MonthlyCategory,
        monthlyDataKey: String,
        data: List<Pair<String, String>>,
    ) {
        withContext(Dispatchers.Main) {
            if (!allTableData.value.containsKey(monthlyCategory)) {
                // 沒有 MonthlyCategory
                MonthlyData().let { monthlyData ->
                    val itemData = mutableListOf<Pair<String, String>>()
                    monthlyData.fixedItem.forEach { itemData.add(Pair(it, "")) }
                    monthlyData.data[getMonthlyDateKey()] = itemData
                    allTableData.value[monthlyCategory] = monthlyData
                }
            }

            allTableData.value[monthlyCategory]?.data?.set(monthlyDataKey, data)
            setMonthlyData(false)
        }
    }

    fun setCurrentTableData(data: List<Pair<String, String>>) {
        allTableData.value[currentMonthlyCategory.value]?.data?.set(getMonthlyDateKey(), data)
        setMonthlyData()
    }

    fun getTotal(): Int =
        allTableData.value[currentMonthlyCategory.value]?.total?.get(getMonthlyDateKey()) ?: 0

    fun setItemData(item: String) {
        allTableData.value[currentMonthlyCategory.value]?.fixedItem?.add(item)
        setMonthlyData(false)
    }

    fun getItemData(): List<String> =
        allTableData.value[currentMonthlyCategory.value]?.fixedItem ?: listOf()

    private fun setMonthlyData(saveDatabase: Boolean = true) {
        if (!allTableData.value.containsKey(currentMonthlyCategory.value)) {
            // 沒有 MonthlyCategory
            MonthlyData().let { monthlyData ->
                val itemData = mutableListOf<Pair<String, String>>()
                monthlyData.fixedItem.forEach { itemData.add(Pair(it, "")) }
                monthlyData.data[getMonthlyDateKey()] = itemData
                allTableData.value[currentMonthlyCategory.value] = monthlyData
                if (saveDatabase) setMonthlyDao(currentMonthlyCategory.value, monthlyData)
            }
        }
        allTableData.value[currentMonthlyCategory.value]?.let { monthlyData ->
            if (monthlyData.data[getMonthlyDateKey()].isNullOrEmpty()) {
                // 沒有 MonthlyDataKey
                val itemData = mutableListOf<Pair<String, String>>()
                monthlyData.fixedItem.forEach { itemData.add(Pair(it, "")) }
                monthlyData.data[getMonthlyDateKey()] = itemData
                selectedTableData.value = itemData
            } else {
                monthlyData.data[getMonthlyDateKey()]?.let {
                    selectedTableData.value = it
                }
            }
            setTotal()
            setNetWorth()
            if (saveDatabase) setMonthlyDao(currentMonthlyCategory.value, monthlyData)
            return
        }
        selectedTableData.value = listOf()
        setTotal()
        setNetWorth()
    }

    private fun getMonthlyDateKey() = "${selectedDate.value.first}/${selectedDate.value.second}"

    // 儲存總共
    private fun setTotal() {
        var total = 0
        selectedTableData.value.forEach {
            if (it.second != "") {
                total += it.second.toIntOrNull() ?: 0
            }
        }
        allTableData.value[currentMonthlyCategory.value]?.total?.set(getMonthlyDateKey(), total)
    }

    // 儲存淨值
    private fun setNetWorth() {
        netWorth.value = 0
        allTableData.value.forEach { total ->
            total.value.total.forEach {
                if (it.key == getMonthlyDateKey()) {
                    when (total.key) {
                        MonthlyCategory.Income, MonthlyCategory.Invest -> netWorth.value += it.value
                        MonthlyCategory.Debt, MonthlyCategory.Expenditure -> netWorth.value -= it.value
                    }
                }
            }
        }
    }

    private fun setMonthlyDao(monthlyCategory: MonthlyCategory, monthlyData: MonthlyData?) {
        monthlyData?.let { tableData ->
            Graph.allCategoryDetailsDao.addDetails(
                CategoryDetails(
                    category = monthlyCategory.name,
                    date = getMonthlyDateKey(),
                    details = mutableStateMapOf<String, String>().apply {
                        tableData.data[getMonthlyDateKey()]?.forEach {
                            this[it.first] = it.second
                        }
                    }.toMap(),
                    project = "",
                )
            )
        }
    }
}

data class MonthlyData(
    var total: SnapshotStateMap<String, Int> = mutableStateMapOf(),
    var data: SnapshotStateMap<String, List<Pair<String, String>>> = mutableStateMapOf(),
    var fixedItem: MutableList<String> = mutableListOf(),
)

enum class MonthlyCategory(
    val categoryName: Int,
) {
    Income(R.string.income),  // 收入
    Invest(R.string.invest),  // 投資
    Expenditure(R.string.expenditure),   // 支出
    Debt(R.string.debt)  // 負債
}