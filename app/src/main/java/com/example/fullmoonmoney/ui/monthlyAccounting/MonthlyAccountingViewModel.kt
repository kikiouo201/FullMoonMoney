package com.example.fullmoonmoney.ui.monthlyAccounting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.AllAssetDetails
import com.example.fullmoonmoney.data.AssetDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class MonthlyAccountingViewModel(
    private val allAssetDetailsDao: AllAssetDetails = Graph.allAssetDetailsDao,
) : ViewModel() {

    private var _currentCategory = AssetCategory.Income
    private val _selectedDate = MutableStateFlow(Pair(2023, 1)) // 日期
    private val _state = MutableStateFlow(MonthlyAccountingViewState())

    val state: StateFlow<MonthlyAccountingViewState>
        get() = _state

    var targetPrice = mutableStateOf(30000)
    var currentMoney = mutableStateOf(2000)
    var monthTargetPrice = mutableStateOf(5000)
    val categories: List<AssetCategory> = listOf(
        AssetCategory.Income, AssetCategory.Invest, AssetCategory.Expenditure, AssetCategory.Debt
    )

    init {
        getStateData()
    }

    private fun getStateData() {
        viewModelScope.launch {
            combine(
                allAssetDetailsDao.getAssetDetail(
                    _currentCategory,
                    getSelectedDataKey()
                ),
                allAssetDetailsDao.getAssetDateDetails(getSelectedDataKey())
                    .transform { detail ->
                        emit(detail.sumOf { it.amount.toIntOrNull() ?: 0 })
                    },
                _selectedDate,
            ) { assetData, netWorth, date ->
                MonthlyAccountingViewState(
                    assetCategory = _currentCategory,
                    date = date,
                    detailList = assetData,
                    netWorth = netWorth,
                )
            }.collect { _state.value = it }
        }
    }

    fun setCurrentCategory(assetCategory: AssetCategory) {
        _currentCategory = assetCategory
        getStateData()
    }

    fun setCurrentDate(date: Pair<Int, Int>) {
        _selectedDate.value = date
        getStateData()
    }

    fun getTotal(): Int = state.value.detailList.sumOf { it.amount.toIntOrNull() ?: 0 }

    fun setItemData(item: String) {
        // todo 存 item
    }

    fun getItemData(): List<String> = _state.value.detailList.map { it.item }

    private fun getSelectedDataKey() = "${_selectedDate.value.first}/${_selectedDate.value.second}"

    // 存資產明細
    fun setAssetDetail(assetDetail: AssetDetail, amount: String) {
        viewModelScope.launch {
            assetDetail.amount = amount
            allAssetDetailsDao.addAssetDetail(
                assetDetail
            )
        }
    }

    fun setAssetDetail(item: String, amount: String) {
        viewModelScope.launch {
            allAssetDetailsDao.addAssetDetail(
                AssetDetail(
                    category = _currentCategory.name,
                    date = getSelectedDataKey(),
                    item = item,
                    amount = amount,
                )
            )
        }
    }
}

// 資產類別明細
data class MonthlyAccountingViewState(
    val assetCategory: AssetCategory = AssetCategory.Income,
    val date: Pair<Int, Int> = Pair(2023, 1), // pair<年,月>
    val detailList: List<AssetDetail> = emptyList(),
    val netWorth: Int = 0,
    val monthTargetPrice: Int = 0,
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