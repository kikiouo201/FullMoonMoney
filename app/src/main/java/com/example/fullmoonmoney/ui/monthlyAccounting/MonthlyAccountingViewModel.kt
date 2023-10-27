package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.CategoryDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyAccountingViewModel : ViewModel() {

    private var allAssetData =
        mutableStateOf(mutableStateMapOf<AssetCategory, AssetCategoryDetail>())
    var currentCategory = mutableStateOf(AssetCategory.Income)
    var targetPrice = mutableStateOf(30000)
    var netWorth = mutableStateOf(0)
    var currentMoney = mutableStateOf(2000)
    var monthTargetPrice = mutableStateOf(5000)
    var selectedDate = mutableStateOf(Pair(2023, 1)) // 日期
    var selectedAssetData = mutableStateOf(listOf<Pair<String, String>>()) // pair<項目,金額>
    val categories: List<AssetCategory> = listOf(
        AssetCategory.Income,
        AssetCategory.Invest,
        AssetCategory.Expenditure,
        AssetCategory.Debt
    )

    init {
        // 測試資料
        AssetCategoryDetail().let {
            it.data["2023/1"] = listOf(Pair("A 銀行", ""), Pair("B 銀行", ""), Pair("現金", ""))
            it.data["2023/9"] = listOf(Pair("B 銀行", ""), Pair("現金", ""))
            it.itemTitles = mutableListOf("A 銀行", "B 銀行", "現金")
            allAssetData.value[AssetCategory.Income] = it
        }
        GlobalScope.launch(Dispatchers.IO) {
            Graph.allCategoryDetailsDao.getDetails { assetCategory, monthlyDataKey, assetData ->
                GlobalScope.launch(Dispatchers.IO) {
                    initAssetData(AssetCategory.valueOf(assetCategory), monthlyDataKey, assetData)
                }
            }
        }
    }

    fun setCurrentStatus(assetCategory: AssetCategory, date: Pair<Int, Int>) {
        currentCategory.value = assetCategory
        selectedDate.value = date
        setMonthlyData(false)
    }

    private suspend fun initAssetData(
        assetCategory: AssetCategory,
        monthlyDataKey: String,
        data: List<Pair<String, String>>,
    ) {
        withContext(Dispatchers.Main) {
            if (!allAssetData.value.containsKey(assetCategory)) {
                // 沒有 MonthlyCategory
                AssetCategoryDetail().let { monthlyData ->
                    val itemData = mutableListOf<Pair<String, String>>()
                    monthlyData.itemTitles.forEach { itemData.add(Pair(it, "")) }
                    monthlyData.data[getMonthlyDateKey()] = itemData
                    allAssetData.value[assetCategory] = monthlyData
                }
            }

            allAssetData.value[assetCategory]?.data?.set(monthlyDataKey, data)
            setMonthlyData(false)
        }
    }

    fun setCurrentTableData(data: List<Pair<String, String>>) {
        allAssetData.value[currentCategory.value]?.data?.set(getMonthlyDateKey(), data)
        setMonthlyData()
    }

    fun getTotal(): Int =
        allAssetData.value[currentCategory.value]?.total?.get(getMonthlyDateKey()) ?: 0

    fun setItemData(item: String) {
        allAssetData.value[currentCategory.value]?.itemTitles?.add(item)
        setMonthlyData(false)
    }

    fun getItemData(): List<String> =
        allAssetData.value[currentCategory.value]?.itemTitles ?: listOf()

    private fun setMonthlyData(saveDatabase: Boolean = true) {
        if (!allAssetData.value.containsKey(currentCategory.value)) {
            // 沒有選定類別的細項
            AssetCategoryDetail().let { detail ->
                detail.data[getMonthlyDateKey()] = detail.itemTitles.map { Pair(it, "") }
                allAssetData.value[currentCategory.value] = detail
                if (saveDatabase) setMonthlyDao(currentCategory.value, detail)
            }
        }
        allAssetData.value[currentCategory.value]?.let { detail ->
            if (detail.data[getMonthlyDateKey()].isNullOrEmpty()) {
                // 有選定類別, 沒有月份的細項
                detail.itemTitles.map { Pair(it, "") }.let {
                    detail.data[getMonthlyDateKey()] = it
                    selectedAssetData.value = it
                }
            } else {
                // 有選定類別, 有月份的細項
                detail.data[getMonthlyDateKey()]?.let {
                    selectedAssetData.value = it
                }
            }
            setTotal()
            setNetWorth()
            if (saveDatabase) setMonthlyDao(currentCategory.value, detail)
            return
        }
        selectedAssetData.value = listOf()
        setTotal()
        setNetWorth()
    }

    private fun getMonthlyDateKey() = "${selectedDate.value.first}/${selectedDate.value.second}"

    // 儲存總共
    private fun setTotal() {
        allAssetData.value[currentCategory.value]?.total?.set(getMonthlyDateKey(),
            selectedAssetData.value.sumOf { it.second.toIntOrNull() ?: 0 })
    }

    // 儲存淨值
    private fun setNetWorth() {
        netWorth.value = 0
        allAssetData.value.forEach { total ->
            total.value.total.forEach {
                if (it.key == getMonthlyDateKey()) {
                    when (total.key) {
                        AssetCategory.Income, AssetCategory.Invest -> netWorth.value += it.value
                        AssetCategory.Debt, AssetCategory.Expenditure -> netWorth.value -= it.value
                    }
                }
            }
        }
    }

    private fun setMonthlyDao(monthlyCategory: AssetCategory, monthlyData: AssetCategoryDetail?) {
        monthlyData?.let { tableData ->
            Graph.allCategoryDetailsDao.addDetails(
                CategoryDetails(
                    category = monthlyCategory.name,
                    date = getMonthlyDateKey(),
                    details = mutableStateMapOf<String, String>().apply {
                        tableData.data[getMonthlyDateKey()]?.forEach {
                            this[it.first] = it.second
                        }
                    }.toMap(),
                    project = "",
                )
            )
        }
    }
}

// 資產類別明細
data class AssetCategoryDetail(
    var total: SnapshotStateMap<String, Int> = mutableStateMapOf(), // map<月份，類別總金額>
    var data: SnapshotStateMap<String, List<Pair<String, String>>> = mutableStateMapOf(), // map<月份，pair<項目,金額>>
    var itemTitles: MutableList<String> = mutableListOf(), // 類別項目們
)

// 資產類別
enum class AssetCategory(
    val categoryName: Int,
) {
    Income(R.string.income),  // 收入
    Invest(R.string.invest),  // 投資
    Expenditure(R.string.expenditure),   // 支出
    Debt(R.string.debt)  // 負債
}