package com.example.fullmoonmoney.ui.project

import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProjectViewState())
    private var _projectCategory = ProjectCategory.FixedIncomeAndExpenditure

    val projectList: List<ProjectCategory> = listOf(
        ProjectCategory.FixedIncomeAndExpenditure,
        ProjectCategory.LivingExpensesBudget,
        ProjectCategory.TargetSavings,
        ProjectCategory.Invest
    )
    val state: StateFlow<ProjectViewState>
        get() = _state

    init {
        getStateData()
    }

    // 測試資料
    private fun getStateData() {
        _state.value = ProjectViewState(
            fixedItemList = listOf(
                FixedItem(
                    item = "房租",
                    price = 20000,
                    project = ProjectCategory.FixedIncomeAndExpenditure,
                    cycle = "每月(3日)",
                    source = "永豐銀行",
                    isExpenditure = false
                ),
                FixedItem(
                    item = "訂閱YouTube",
                    price = 20000,
                    project = ProjectCategory.FixedIncomeAndExpenditure,
                    cycle = "每月(15日)",
                    source = "永豐銀行",
                    isExpenditure = true
                )
            )
        )
    }

    fun setSelectedProject(projectCategory: ProjectCategory) {
        _projectCategory = projectCategory
    }
}

enum class ProjectCategory(
    val categoryName: Int,
) {
    FixedIncomeAndExpenditure(R.string.fixed_income_and_expenditure),  // 固定收支
    LivingExpensesBudget(R.string.living_expenses_budget),   // 生活費預算
    TargetSavings(R.string.target_savings),  // 目標儲蓄
    Invest(R.string.invest),  // 投資
}

data class FixedItem(
    val item: String = "",
    val price: Int = 0,
    val project: ProjectCategory = ProjectCategory.FixedIncomeAndExpenditure,
    val cycle: String = "", // 週期
    val source: String = "", // 錢的來源
    val isExpenditure: Boolean = false, // 是否為支出
)

data class ProjectViewState(
    val projectCategory: ProjectCategory = ProjectCategory.FixedIncomeAndExpenditure,
    val date: Pair<Int, Int> = Pair(2023, 1), // pair<年,月>
    val fixedItemList: List<FixedItem> = emptyList(),
    val fixedIncomePrice: Int = 0,
    val fixedExpenditurePrice: Int = 0,
)