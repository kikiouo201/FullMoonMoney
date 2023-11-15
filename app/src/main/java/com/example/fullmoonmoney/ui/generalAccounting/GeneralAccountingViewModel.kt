package com.example.fullmoonmoney.ui.generalAccounting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullmoonmoney.Graph
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.data.AccountingDetail
import com.example.fullmoonmoney.data.AllAccountingDetail
import com.example.fullmoonmoney.data.AllProject
import com.example.fullmoonmoney.data.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class GeneralAccountingViewModel(
    private val allAllAccountingDetailDao: AllAccountingDetail = Graph.allAccountingDetailDao,
    private val allProjectDao: AllProject = Graph.allProjectDao,
) : ViewModel() {

    private val _state = MutableStateFlow(GeneralAccountingViewState())
    private val _selectedDate = MutableStateFlow(Pair(2023, 1)) // 日期
    private var _selectedProject = MutableStateFlow<Project?>(null)
    private var selectedDate = mutableStateOf(Pair(2023, 1))
    private var _isCategory = false

    val state: StateFlow<GeneralAccountingViewState>
        get() = _state

    init {
        initTestData()
        getStateData()
    }

    // 測試資料
    private fun initTestData() {
        val projectList = mutableListOf("午餐", "晚餐")
        val listOne = (0..3).map {
            AccountingDetail(
                item = "麥當當$it",
                amount = 120,
                date = getSelectedDataKey(),
                category = AccountingCategory.Project.name,
                projectList = projectList
            )
        }
        val listTwo = (0..3).map {
            AccountingDetail(
                item = "${it}麥SS",
                amount = 120,
                date = getSelectedDataKey(),
                category = AccountingCategory.Detail.name,
                projectList = projectList
            )
        }
        val listThree = projectList.map {
            AccountingDetail(
                item = "麥當當$it",
                amount = 120,
                date = getSelectedDataKey(),
                category = AccountingCategory.Project.name,
                projectList = mutableListOf(it)
            )
        }
        viewModelScope.launch {
            allAllAccountingDetailDao.addDetailList(listOne)
            allAllAccountingDetailDao.addDetailList(listTwo)
            allAllAccountingDetailDao.addDetailList(listThree)
        }
        setProjectList(projectList.map { Project(name = it) })
        setProject(Project(name = projectList.first()))
    }

    private fun getStateData() {
        viewModelScope.launch {
            combine(
                allAllAccountingDetailDao.getDetailList(
                    if (_isCategory) AccountingCategory.Project else AccountingCategory.Detail,
                    getSelectedDataKey()
                ).transform { list ->
                    emit(
                        if (_isCategory)
                            list.filter { detail ->
                                _selectedProject.value?.let {
                                    detail.projectList.contains(it.name)
                                } ?: false
                            }
                        else
                            list
                    )
                },
                allProjectDao.getAllProject(),
                _selectedDate,
                _selectedProject,
            ) { assetData, allProject, date, project ->
                GeneralAccountingViewState(
                    isCategory = _isCategory,
                    date = date,
                    selectedProject = project,
                    detailList = assetData,
                    projectList = allProject
                )
            }.collect { _state.value = it }
        }
    }

    // 明細的專案
    fun setAllProject(projectName: String) {
        viewModelScope.launch {
            allProjectDao.addProject(Project(name = projectName))
            getStateData()
        }
    }

    // 選擇的專案
    fun setSelectedProject(project: Project) {
        _selectedProject.value = project
        getStateData()
    }

    // 一般記帳的分類
    fun setSelectedAccountingCategory(isCategory: Boolean) {
        _isCategory = isCategory
        getStateData()
    }

    fun setCurrentStatus(date: Pair<Int, Int>) {
        selectedDate.value = date
        getStateData()
    }

    fun setAccountingDetailDao(accountingDetail: AccountingDetail) {
        viewModelScope.launch {
            allAllAccountingDetailDao.addDetail(accountingDetail)
        }
    }

    fun getTotal(): Int = state.value.detailList.sumOf { it.amount }

    fun getSelectedDataKey() = "${selectedDate.value.first}/${selectedDate.value.second}"

    private fun setProject(project: Project) {
        _selectedProject.value = project
        getStateData()
    }

    private fun setProjectList(projectList: List<Project>) {
        viewModelScope.launch {
            allProjectDao.addAllProject(projectList)
        }
    }
}

data class GeneralAccountingViewState(
    val selectedProject: Project? = null,
    var isCategory: Boolean = false,
    val date: Pair<Int, Int> = Pair(2023, 1), // pair<年,月>
    val detailList: List<AccountingDetail> = emptyList(),
    val projectList: List<Project> = emptyList(),
)

enum class AccountingCategory(
    val categoryName: Int,
) {
    Project(R.string.project),  // 專案
    Detail(R.string.detail),  // 明細
}