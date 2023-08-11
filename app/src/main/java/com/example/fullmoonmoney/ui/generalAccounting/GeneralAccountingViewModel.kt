package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GeneralAccountingViewModel : ViewModel() {
    val total = mutableStateOf(0)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    var selectedTableData = mutableStateOf(mutableListOf<AccountingDetail>())

    init {
        // 測試資料
        val project = mutableListOf("午餐")
        val accountingDetail = AccountingDetail(
            itemName = "麥當勞",
            price = 120,
            project = project
        )
        selectedTableData.value.add(accountingDetail)
        selectedTableData.value.add(accountingDetail)
        selectedTableData.value.add(accountingDetail)
        selectedTableData.value.forEach {
            total.value += it.price
        }
    }
}

data class AccountingDetail(
    var price: Int = 0,
    var itemName: String = "",
    var project: MutableList<String> = mutableListOf()
)