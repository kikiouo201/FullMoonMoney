package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MonthlyAccountingViewModel : ViewModel() {
    var tableData = mutableStateOf(listOf(Pair("A 銀行", ""), Pair("B 銀行", ""), Pair("現金", "")))
}