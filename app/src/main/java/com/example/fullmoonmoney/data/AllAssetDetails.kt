package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.AssetDetailsDao
import com.example.fullmoonmoney.ui.monthlyAccounting.AssetCategory
import kotlinx.coroutines.flow.Flow

class AllAssetDetails(
    private val assetDetailsDao: AssetDetailsDao,
) {
    suspend fun addAssetDetail(assetDetail: AssetDetail) {
        assetDetailsDao.insert(assetDetail)
    }

    suspend fun addAssetDetailList(assetDetailList: List<AssetDetail>) {
        assetDetailsDao.insertAll(assetDetailList)
    }

    fun getAssetDetail(category: AssetCategory, date: String): Flow<List<AssetDetail>> {
        return assetDetailsDao.findByDetails(category.name, date)
    }

    fun getAssetDateDetails(date: String): Flow<List<AssetDetail>> {
        return assetDetailsDao.findByDateDetails(date)
    }
}