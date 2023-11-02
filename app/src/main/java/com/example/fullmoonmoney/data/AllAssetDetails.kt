package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.AssetDetailsDao
import com.example.fullmoonmoney.ui.monthlyAccounting.AssetCategory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllAssetDetails(
    private val assetDetailsDao: AssetDetailsDao,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun addAssetDetail(assetDetail: AssetDetail) {
        GlobalScope.launch(Dispatchers.IO) {
            assetDetailsDao.insert(assetDetail)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getAssetDetail(
        category: AssetCategory,
        date: String,
        initAssetDetail: (category: AssetCategory, date: String, List<Pair<String, String>>) -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            initAssetDetail(
                category,
                date,
                assetDetailsDao.findByDetails(category.name, date)
                    .map { Pair(it.item, it.amount) }
            )
        }
    }
}