package com.example.fullmoonmoney.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var state = mutableStateOf(0)
    val homeCategories: List<HomeCategory> = listOf(HomeCategory.Monthly, HomeCategory.General, HomeCategory.Assets, HomeCategory.Project)
}

enum class HomeCategory {
    Monthly,  // 月記帳
    General,  // 一般記帳
    Assets,   // 資產
    Project,  // 專案
}