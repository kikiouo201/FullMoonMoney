package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GeneralAccountingViewModel : ViewModel() {

    private var allTableData = mutableStateMapOf<String, GeneralAccountingItem>()
    private var selectedTotal = mutableStateOf(0)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    val selectedCategory = mutableStateOf("")
    var selectedTableData = mutableStateOf<GeneralAccountingItem?>(null)
    var categoryList = mutableStateOf(listOf<Category>())

    init {
        // 測試資料
        val projectList = mutableListOf("午餐", "晚餐")
        val list = (0..3).map {
            AccountingDetail(
                itemName = "麥當勞",
                price = 120,
                projectList = projectList
            )
        }
        categoryList.value = projectList.map { Category(name = it) }
        allTableData[getSelectedDataKey()] = GeneralAccountingItem(list.sumOf { it.price }, list)
        setSelectedCategory()

        GlobalScope.launch(Dispatchers.IO) {
            Graph.allCategoryDao.addCategory(Category(name = "早餐"))
            Graph.allCategoryDao.addCategory(Category(name = "午餐"))
            Graph.allCategoryDao.addCategory(Category(name = "晚餐"))
            Graph.allCategoryDao.getAllCategory { allCategory ->
                GlobalScope.launch(Dispatchers.Main) {
                    categoryList.value = allCategory
                    categoryList.value.getOrNull(0)?.let {
                        selectedCategory.value = it.name
                    }
                }
            }
        }
    }

    fun setCurrentCategory(data: String) {
        mutableListOf<Category>().let {
            it.addAll(categoryList.value)
            it.add(Category(name = data))
            categoryList.value = it
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
        allTableData[getSelectedDataKey()]?.let { item ->
            val detailList = mutableListOf<AccountingDetail>().apply {
                addAll(item.detailList)
                add(data)
            }
            GeneralAccountingItem(detailList = detailList).let {
                it.total = setTotal(it)
                allTableData[getSelectedDataKey()] = it
                selectedTableData.value = it
                selectedTotal.value = it.total
            }
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
    var detailList: List<AccountingDetail> = listOf(),
)

data class AccountingDetail(
    var price: Int = 0,
    var itemName: String = "",
    var projectList: MutableList<String> = mutableListOf(),
)