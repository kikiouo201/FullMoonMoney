package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.CategoryDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllCategory(
    private val categoryDao: CategoryDao,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun addCategory(category: Category) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insert(category)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addAllCategory(categoryList: List<Category>) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insertAll(categoryList)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getCategory(initCategory: (String) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.getAll().forEach { initCategory(it.name) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getAllCategory(initAllCategory: (List<Category>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            initAllCategory(categoryDao.getAll())
        }
    }
}