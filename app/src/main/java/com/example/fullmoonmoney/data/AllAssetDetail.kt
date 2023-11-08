package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.AssetDetailDao
import com.example.fullmoonmoney.ui.monthlyAccounting.AssetCategory
import kotlinx.coroutines.flow.Flow

class AllAssetDetail(
    private val assetDetailDao: AssetDetailDao,
) {
    suspend fun addDetail(assetDetail: AssetDetail) {
        assetDetailDao.insert(assetDetail)
    }

    suspend fun addDetailList(assetDetailList: List<AssetDetail>) {
        assetDetailDao.insertAll(assetDetailList)
    }

    fun getDetailList(category: AssetCategory, date: String): Flow<List<AssetDetail>> {
        return assetDetailDao.findByDetailList(category.name, date)
    }

    fun getDateDetailList(date: String): Flow<List<AssetDetail>> {
        return assetDetailDao.findByDateDetailList(date)
    }
}