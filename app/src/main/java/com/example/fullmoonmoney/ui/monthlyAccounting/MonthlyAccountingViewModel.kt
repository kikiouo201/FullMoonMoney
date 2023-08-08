package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R

class MonthlyAccountingViewModel : ViewModel() {

    var netWorth = mutableStateOf(0)
    private var monthlyData = mutableStateOf(mutableStateMapOf<MonthlyCategory, MonthlyData>())
    var selectedDate = mutableStateOf(Pair(2023, 1))
    private var currentMonthlyCategory = mutableStateOf(MonthlyCategory.Income)
    var monthlyCategory = mutableStateOf(MonthlyCategory.Income)
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
            monthlyData.value[MonthlyCategory.Income] = it
        }
    }

    fun setCurrentStatus(monthlyCategory: MonthlyCategory, date: Pair<Int, Int>) {
        currentMonthlyCategory.value = monthlyCategory
        selectedDate.value = date
    }

    fun setCurrentTableData(
        data: List<Pair<String, String>>,
        monthlyCategory: MonthlyCategory = currentMonthlyCategory.value
    ) {
        monthlyData.value[monthlyCategory]?.data?.set(getMonthlyDataKey(), data)
    }

    fun getMonthlyData(): List<Pair<String, String>> {
        if (!monthlyData.value.containsKey(currentMonthlyCategory.value)) {
            // 沒有MonthlyCategory
            MonthlyData().let {
                it.data[selectedDate.value.second.toString()] = listOf()
                monthlyData.value[currentMonthlyCategory.value] = it
            }
        }
        monthlyData.value[currentMonthlyCategory.value]?.data?.get(getMonthlyDataKey())
            ?.let { return it }
        return listOf()
    }

    private fun getMonthlyDataKey(): String {
        return "${selectedDate.value.first}/${selectedDate.value.second}"
    }
}

data class MonthlyData(
    var data: SnapshotStateMap<String, List<Pair<String, String>>> = mutableStateMapOf()
)

enum class MonthlyCategory(
    val categoryName: Int
) {
    Income(R.string.income),  // 收入
    Invest(R.string.invest),  // 投資
    Expenditure(R.string.expenditure),   // 支出
    Debt(R.string.debt)  // 負債
}