package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GeneralAccountingViewModel : ViewModel() {

    private var allTableData = mutableStateMapOf<String, GeneralAccountingItem>()
    private var selectedTotal = mutableStateOf(0)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    val selectedCategory = mutableStateOf("")
    var selectedTableData = mutableStateOf<GeneralAccountingItem?>(null)
    val categoryList = mutableStateOf(listOf<String>())

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
        categoryList.value = listOf("午餐", "晚餐", "早餐")
        selectedCategory.value = categoryList.value[0]
        allTableData[getSelectedDataKey()] = GeneralAccountingItem(total, list)
        setSelectedCategory()
    }

    fun setCurrentCategory(data: String) {
        categoryList.value.let {
            val detailList = mutableListOf<String>()
            detailList.addAll(categoryList.value)
            detailList.add(data)
            categoryList.value = detailList
        }
    }

    fun setSelectedCategory(data: String) {
        selectedCategory.value = data
    }

    fun setCurrentStatus(date: Pair<Int, Int>) {
        selectedDate.value = date
        setSelectedCategory()
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

    fun getTotal(): Int = selectedTableData.value?.total ?: 0

    private fun setTotal(generalAccounting: GeneralAccountingItem): Int {
        var total = 0
        generalAccounting.detailList.forEach {
            total += it.price
        }
        return total
    }

    private fun getSelectedDataKey() = "${selectedDate.value.first}/${selectedDate.value.second}"

    private fun setSelectedCategory() {
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
    var detailList: MutableList<AccountingDetail> = mutableListOf(),
)

data class AccountingDetail(
    var price: Int = 0,
    var itemName: String = "",
    var project: MutableList<String> = mutableListOf(),
)