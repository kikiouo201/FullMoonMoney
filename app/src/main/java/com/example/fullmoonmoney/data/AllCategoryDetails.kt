package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.CategoryDetailsDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllCategoryDetails(
    private val categoryDetailsDao: CategoryDetailsDao,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun addDetails(details: CategoryDetails) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDetailsDao.insert(details)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getDetails(initDetails: (String, String, List<Pair<String, String>>?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDetailsDao.getAll().forEach {
                initDetails(
                    it.category,
                    it.date,
                    it.details?.map { map -> Pair(map.key, map.value) }
                )
            }
        }
    }
}