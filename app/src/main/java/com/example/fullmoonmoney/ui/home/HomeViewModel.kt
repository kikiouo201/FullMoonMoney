package com.example.fullmoonmoney.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.monthlyAccounting.MonthlyCategory

class HomeViewModel : ViewModel() {
    var state = mutableStateOf(0)
    var monthlyCategory = mutableStateOf(MonthlyCategory.Income)
    val homeCategories: List<HomeCategory> = listOf(
        HomeCategory.Monthly,
        HomeCategory.General,
        HomeCategory.Assets,
        HomeCategory.Project
    )
}

enum class HomeCategory(
    val categoryName: Int,
    val icon: ImageVector
) {
    Monthly(R.string.monthly_accounting, Icons.Outlined.DateRange),  // 月記帳
    General(R.string.general_accounting, Icons.Outlined.List),  // 一般記帳
    Assets(R.string.assets, Icons.Outlined.ShoppingCart),   // 資產
    Project(R.string.project, Icons.Outlined.Star)  // 專案
}