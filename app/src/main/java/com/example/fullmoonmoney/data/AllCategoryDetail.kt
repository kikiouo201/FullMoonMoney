package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.CategoryDetailDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllCategoryDetail(
    private val categoryDetailDao: CategoryDetailDao,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun addDetailList(detail: CategoryDetail) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDetailDao.insert(detail)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getDetailList(initDetail: (String, List<Pair<String, String>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDetailDao.getAll().forEach { allDetail ->
                allDetail.detailList?.map { map -> Pair(map.key, map.value) }?.let {
                    initDetail(
                        allDetail.category,
                        it
                    )
                }
            }
        }
    }
}