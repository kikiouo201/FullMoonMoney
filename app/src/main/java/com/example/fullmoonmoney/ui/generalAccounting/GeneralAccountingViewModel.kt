package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.data.Category
import com.example.fullmoonmoney.data.CategoryDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GeneralAccountingViewModel : ViewModel() {

    private var allItem = mutableStateMapOf<String, AccountingItem>()
    private var selectedTotal = mutableStateOf(0)
    var selectedDate = mutableStateOf(Pair(2023, 1))
    val selectedCategory = mutableStateOf<Category?>(null)
    var selectedItem = mutableStateOf<AccountingItem?>(null)
    var categoryList = mutableStateOf(listOf<Category>())

    init {
        // 測試資料
        val categoryNameList = mutableListOf(
            "早餐",
            "午餐",
            "晚餐",
            "早餐1",
            "午餐1",
            "晚餐1",
            "早餐2",
            "午餐2",
            "晚餐2"
        )
        val projectList = mutableListOf("午餐", "晚餐")
        val list = (0..3).map {
            AccountingDetail(
                itemName = "麥當勞$it",
                price = 120,
                projectList = projectList
            )
        }
        categoryList.value = projectList.map { Category(name = it) }
        selectedCategory.value = categoryList.value.first()
        allItem[getSelectedDataKey()] = AccountingItem(list.sumOf { it.price }, list)
        setSelectedCategory()

        GlobalScope.launch(Dispatchers.IO) {
            list.forEachIndexed { index, accountingDetail ->
                setAccountingDetailDao(Category(name = categoryNameList[index]), accountingDetail)
            }
            Graph.allCategoryDao.getAllCategory { allCategory ->
                GlobalScope.launch(Dispatchers.Main) {
                    if (allCategory.isNotEmpty()) {
                        categoryList.value = allCategory
                        categoryList.value.getOrNull(0)?.let {
                            setSelectedCategory(it)
                        }
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

    fun setSelectedCategory(category: Category) {
        selectedCategory.value = category
        GlobalScope.launch(Dispatchers.IO) {
            Graph.allCategoryDetailDao.getDetailList { assetCategory, assetData ->
                GlobalScope.launch(Dispatchers.IO) {
                    initDetails(
                        Category(name = assetCategory),
                        assetData
                    )
                }
            }
        }
    }

    fun setCurrentStatus(date: Pair<Int, Int>) {
        selectedDate.value = date
        setSelectedCategory()
    }

    fun setCurrentTableData(data: AccountingDetail) {
        allItem[getSelectedDataKey()]?.let { item ->
            if (item.detailList.isEmpty()) return
            val detailList = mutableListOf<AccountingDetail>().apply {
                addAll(item.detailList)
                add(data)
            }
            AccountingItem(detailList = detailList).let {
                it.total = setTotal(it)
                allItem[getSelectedDataKey()] = it
                selectedItem.value = it
                selectedTotal.value = it.total
            }
        }
    }

    private fun setAccountingDetailDao(category: Category, accountingDetail: AccountingDetail) {
        accountingDetail.let { tableData ->
            Graph.allCategoryDetailDao.addDetailList(
                CategoryDetail(
                    category = category.name,
                    date = "2023/1",
                    detailList = mapOf(Pair(tableData.itemName, tableData.price.toString())),
                    project = tableData.projectList.first(),
                )
            )
        }
    }

    fun getTotal(): Int = selectedItem.value?.total ?: 0

    private fun setTotal(generalAccounting: AccountingItem): Int {
        var total = 0
        generalAccounting.detailList.forEach {
            total += it.price
        }
        return total
    }

    private fun getSelectedDataKey() = "${selectedDate.value.first}/${selectedDate.value.second}"

    private fun setSelectedCategory() {
        if (allItem[getSelectedDataKey()] == null) {
            allItem[getSelectedDataKey()] = AccountingItem()
            selectedItem.value = AccountingItem()
            selectedTotal.value = 0
        } else {
            allItem[getSelectedDataKey()]?.let {
                selectedItem.value = it
                selectedTotal.value = it.total
            }
        }
    }

    private fun initDetails(
        assetCategory: Category,
        data: List<Pair<String, String>>,
    ) {
        val detailList = mutableListOf<AccountingDetail>()
        val accountingDetail = AccountingDetail()
        accountingDetail.let { detail ->
            data.forEach {
                detail.itemName = it.first
                detail.price = it.second.toIntOrNull() ?: 0
                detail.projectList = mutableListOf(assetCategory.name)
            }
        }
        detailList.add(accountingDetail)
        if (detailList.isEmpty()) return
        setCurrentTableData(accountingDetail)
    }
}

// 記帳項目
data class AccountingItem(
    var total: Int = 0,
    var detailList: List<AccountingDetail> = listOf(),
)

// 記帳明細
data class AccountingDetail(
    var price: Int = 0,
    var itemName: String = "",
    var projectList: MutableList<String> = mutableListOf(),
)