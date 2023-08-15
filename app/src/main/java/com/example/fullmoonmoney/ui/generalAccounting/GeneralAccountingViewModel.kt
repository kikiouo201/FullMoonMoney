package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel

class GeneralAccountingViewModel : ViewModel() {

    private var allTableData: SnapshotStateMap<String, GeneralAccountingItem> = mutableStateMapOf()
    var selectedTotal = mutableStateOf(0)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    var selectedTableData: MutableState<GeneralAccountingItem?> = mutableStateOf(null)

    init {
        // 測試資料
        val list = mutableListOf<AccountingDetail>()
        val project = mutableListOf("午餐")
        val accountingDetail = AccountingDetail(
            itemName = "麥當勞",
            price = 120,
            project = project
        )
        var total = 0
        list.add(accountingDetail)
        list.add(accountingDetail)
        list.add(accountingDetail)
        list.forEach {
            total += it.price
        }
        allTableData[getSelectedDataKey()] = GeneralAccountingItem(total, list)
        setSelectedData()
    }

    fun setCurrentStatus(date: Pair<Int, Int>) {
        selectedDate.value = date
        setSelectedData()
    }

    fun setCurrentTableData(data: AccountingDetail) {
        allTableData[getSelectedDataKey()]?.let {
            val detailList = mutableListOf<AccountingDetail>()
            detailList.addAll(it.detailList)
            detailList.add(data)
            val generalAccounting = GeneralAccountingItem(detailList = detailList)
            generalAccounting.total = setTotal(generalAccounting)
            allTableData[getSelectedDataKey()] = generalAccounting
            selectedTableData.value = generalAccounting
            selectedTotal.value = generalAccounting.total
        }
    }

    fun getTotal(): Int {
        return selectedTableData.value?.total ?: 0
    }

    private fun setTotal(generalAccounting: GeneralAccountingItem): Int {
        var total = 0
        generalAccounting.detailList.forEach {
            total += it.price
        }
        return total
    }

    private fun getSelectedDataKey(): String {
        return "${selectedDate.value.first}/${selectedDate.value.second}"
    }

    private fun setSelectedData() {
        if (allTableData[getSelectedDataKey()] == null) {
            allTableData[getSelectedDataKey()] = GeneralAccountingItem()
            selectedTableData.value = GeneralAccountingItem()
            selectedTotal.value = 0
        } else {
            allTableData[getSelectedDataKey()]?.let {
                selectedTableData.value = it
                selectedTotal.value = it.total
            }
        }
    }
}

data class GeneralAccountingItem(
    var total: Int = 0,
    var detailList: MutableList<AccountingDetail> = mutableListOf()
)

data class AccountingDetail(
    var price: Int = 0,
    var itemName: String = "",
    var project: MutableList<String> = mutableListOf()
)