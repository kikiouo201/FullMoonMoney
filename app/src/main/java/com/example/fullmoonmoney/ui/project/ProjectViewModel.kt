package com.example.fullmoonmoney.ui.project

import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProjectViewState())
    private var _projectCategory = ProjectCategory.TargetSavings

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
            projectCategory = _projectCategory,
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
            ),
            budgetItemList = listOf(
                BudgetItem(
                    item = "食物",
                    price = 30000,
                    frequency = 1,
                ),
                BudgetItem(
                    item = "交通",
                    price = 20000,
                    frequency = 2,
                ),
                BudgetItem(
                    item = "居住費",
                    price = 30000,
                    frequency = 3,
                ),
                BudgetItem(
                    item = "生活費",
                    price = 30000,
                    frequency = 3,
                ),
                BudgetItem(
                    item = "娛樂費",
                    price = 30000,
                    frequency = 2,
                ),
                BudgetItem(
                    item = "育兒費",
                    price = 30000,
                    frequency = 2,
                ),
                BudgetItem(
                    item = "孝親費",
                    price = 30000,
                    frequency = 1,
                ),
            )
        )
    }

    fun setSelectedProject(projectCategory: ProjectCategory) {
        _projectCategory = projectCategory
        getStateData()
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

// 固定支出
data class FixedItem(
    val item: String = "",
    val price: Int = 0,
    val project: ProjectCategory = ProjectCategory.FixedIncomeAndExpenditure,
    val cycle: String = "", // 週期
    val source: String = "", // 錢的來源
    val isExpenditure: Boolean = false, // 是否為支出
)

// 預算
data class BudgetItem(
    val item: String = "",
    val price: Int = 0,
    val frequency: Int = 0,
)

data class ProjectViewState(
    val projectCategory: ProjectCategory = ProjectCategory.FixedIncomeAndExpenditure,
    val date: Pair<Int, Int> = Pair(2023, 1), // pair<年,月>
    val budgetItemList: List<BudgetItem> = emptyList(),
    val fixedItemList: List<FixedItem> = emptyList(),
    val fixedIncomePrice: Int = 0,
    val fixedExpenditurePrice: Int = 0,
)