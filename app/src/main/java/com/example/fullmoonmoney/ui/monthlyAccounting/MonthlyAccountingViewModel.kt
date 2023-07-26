package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MonthlyAccountingViewModel : ViewModel() {
    var monthlyData = mutableStateOf(mutableStateMapOf<Int, MonthlyData>())
    var currentTableData = mutableStateOf(listOf<Pair<String, String>>())

    init {
        // 測試資料
        MonthlyData(1).let {
            it.data = listOf(Pair("A 銀行", ""), Pair("B 銀行", ""), Pair("現金", ""))
            monthlyData.value[1] = it
        }
        monthlyData.value[2] = MonthlyData(2)
        MonthlyData(9).let {
            it.data = listOf(Pair("B 銀行", ""))
            monthlyData.value[9] = it
        }
    }

    fun addMonthlyData(index: Int) {
        monthlyData.value[index] = MonthlyData(index)
    }

    fun setCurrentTableData(monthIndex: Int, data: List<Pair<String, String>>) {
        monthlyData.value[monthIndex]?.data = data
        currentTableData.value = data
    }
}

data class MonthlyData(val index: Int) {
    var monthIndex = index + 1
    var data: List<Pair<String, String>> = emptyList()
}