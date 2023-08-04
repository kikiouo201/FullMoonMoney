package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R

class MonthlyAccountingViewModel : ViewModel() {

    private var monthlyData = mutableStateOf(mutableStateMapOf<MonthlyCategory, MonthlyData>())
    private var monthIndex = mutableStateOf(1)
    private var currentMonthlyCategory = mutableStateOf(MonthlyCategory.Income)

    init {
        // 測試資料
        MonthlyData().let {
            it.data[1] = listOf(Pair("A 銀行", ""), Pair("B 銀行", ""), Pair("現金", ""))
            it.data[9] = listOf(Pair("B 銀行", ""), Pair("現金", ""))
            monthlyData.value[MonthlyCategory.Income] = it
        }
    }

    fun setCurrentStatus(monthlyCategory: MonthlyCategory, index: Int) {
        currentMonthlyCategory.value = monthlyCategory
        monthIndex.value = index
    }

    fun setCurrentTableData(
        index: Int = monthIndex.value,
        data: List<Pair<String, String>>,
        monthlyCategory: MonthlyCategory = currentMonthlyCategory.value
    ) {
        monthlyData.value[monthlyCategory]?.data?.set(index, data)
    }

    fun getMonthlyData(): List<Pair<String, String>> {
        if (monthlyData.value.containsKey(currentMonthlyCategory.value)) {
            if (monthlyData.value[currentMonthlyCategory.value]?.data?.containsKey(monthIndex.value) == true) {
                // 都有
                monthlyData.value[currentMonthlyCategory.value]?.data?.get(monthIndex.value)?.let {
                    return it
                }
            } else {
                // 有MonthlyCategory,沒有monthIndex
                monthlyData.value[currentMonthlyCategory.value]?.data?.get(monthIndex.value)?.let {
                    return it
                }
            }
        } else {
            // 沒有MonthlyCategory
            MonthlyData().let {
                it.data[monthIndex.value] = listOf()
                monthlyData.value[currentMonthlyCategory.value] = it
            }
            monthlyData.value[currentMonthlyCategory.value]?.data?.get(monthIndex.value)?.let {
                return it
            }
        }
        return listOf()
    }
}

data class MonthlyData(
    var data: SnapshotStateMap<Int, List<Pair<String, String>>> = mutableStateMapOf()
)

enum class MonthlyCategory(
    val categoryName: Int
) {
    Income(R.string.income),  // 收入
    Invest(R.string.invest),  // 投資
    Expenditure(R.string.expenditure),   // 支出
    Debt(R.string.debt)  // 負債
}