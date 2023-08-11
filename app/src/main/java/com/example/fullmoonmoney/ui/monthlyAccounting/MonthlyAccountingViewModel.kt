package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R

class MonthlyAccountingViewModel : ViewModel() {

    private var allMonthlyData = mutableStateOf(mutableStateMapOf<MonthlyCategory, MonthlyData>())
    var currentMonthlyCategory = mutableStateOf(MonthlyCategory.Income)
    var netWorth = mutableStateOf(0)
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
            it.itemData = mutableListOf("A 銀行", "B 銀行", "現金")
            allMonthlyData.value[MonthlyCategory.Income] = it
        }
        setMonthlyData()
    }

    fun setCurrentStatus(monthlyCategory: MonthlyCategory, date: Pair<Int, Int>) {
        currentMonthlyCategory.value = monthlyCategory
        selectedDate.value = date
        setMonthlyData()
    }

    fun setCurrentTableData(data: List<Pair<String, String>>) {
        allMonthlyData.value[currentMonthlyCategory.value]?.data?.set(getMonthlyDataKey(), data)
        setMonthlyData()
    }

    fun getTotal(): Int {
        return allMonthlyData.value[currentMonthlyCategory.value]?.total?.get(getMonthlyDataKey())
            ?: 0
    }

    fun setItemData(item: String) {
        allMonthlyData.value[currentMonthlyCategory.value]?.itemData?.add(item)
        setMonthlyData()
    }

    fun getItemData(): List<String> {
        return allMonthlyData.value[currentMonthlyCategory.value]?.itemData ?: listOf()
    }

    private fun setMonthlyData() {
        if (!allMonthlyData.value.containsKey(currentMonthlyCategory.value)) {
            // 沒有 MonthlyCategory
            MonthlyData().let { monthlyData ->
                val itemData = mutableListOf<Pair<String, String>>()
                monthlyData.itemData.forEach { itemData.add(Pair(it, "")) }
                monthlyData.data[getMonthlyDataKey()] = itemData
                allMonthlyData.value[currentMonthlyCategory.value] = monthlyData
            }
        }
        allMonthlyData.value[currentMonthlyCategory.value]?.let { monthlyData ->
            if (monthlyData.data[getMonthlyDataKey()].isNullOrEmpty()) {
                // 沒有 MonthlyDataKey
                val itemData = mutableListOf<Pair<String, String>>()
                monthlyData.itemData.forEach { itemData.add(Pair(it, "")) }
                monthlyData.data[getMonthlyDataKey()] = itemData
                selectedTableData.value = itemData
            } else {
                monthlyData.data[getMonthlyDataKey()]?.let {
                    selectedTableData.value = it
                }
            }
            setTotal()
            setNetWorth()
            return
        }
        selectedTableData.value = listOf()
        setTotal()
        setNetWorth()
    }

    private fun getMonthlyDataKey(): String {
        return "${selectedDate.value.first}/${selectedDate.value.second}"
    }

    // 儲存總共
    private fun setTotal() {
        var total = 0
        selectedTableData.value.forEach {
            if (it.second != "") {
                total += it.second.toInt()
            }
        }
        allMonthlyData.value[currentMonthlyCategory.value]?.total?.set(getMonthlyDataKey(), total)
    }

    // 儲存淨值
    private fun setNetWorth() {
        netWorth.value = 0
        allMonthlyData.value.forEach { total ->
            total.value.total.forEach {
                if (it.key == getMonthlyDataKey()) {
                    when (total.key) {
                        MonthlyCategory.Income, MonthlyCategory.Invest -> netWorth.value += it.value
                        MonthlyCategory.Debt, MonthlyCategory.Expenditure -> netWorth.value -= it.value
                    }
                }
            }
        }
    }
}

data class MonthlyData(
    var total: SnapshotStateMap<String, Int> = mutableStateMapOf(),
    var data: SnapshotStateMap<String, List<Pair<String, String>>> = mutableStateMapOf(),
    var itemData: MutableList<String> = mutableListOf()
)

enum class MonthlyCategory(
    val categoryName: Int
) {
    Income(R.string.income),  // 收入
    Invest(R.string.invest),  // 投資
    Expenditure(R.string.expenditure),   // 支出
    Debt(R.string.debt)  // 負債
}