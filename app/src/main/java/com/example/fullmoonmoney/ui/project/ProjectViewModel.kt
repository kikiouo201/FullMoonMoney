package com.example.fullmoonmoney.ui.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProjectViewModel : ViewModel() {

    val selectedData = mutableStateOf("")
    val dataList = mutableStateOf(listOf<String>())
    var selectedDate = mutableStateOf(Pair(2023, 1))

    init {
        dataList.value = listOf("午餐", "晚餐", "早餐")
        selectedData.value = dataList.value[0]
    }

    fun setCurrentStatus(date: Pair<Int, Int>) {
        selectedDate.value = date
    }

    fun setCurrentTableData(data: String) {
        dataList.value.let {
            val detailList = mutableListOf<String>()
            detailList.addAll(dataList.value)
            detailList.add(data)
            dataList.value = detailList
        }
    }

    fun setSelectedData(data: String) {
        selectedData.value = data
    }
}