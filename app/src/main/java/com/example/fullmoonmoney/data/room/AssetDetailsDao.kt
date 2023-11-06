package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.AssetDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDetailsDao : BaseDao<AssetDetail> {
    @Query("SELECT * FROM AssetDetail")
    fun getAll(): List<AssetDetail>

    @Query("SELECT * FROM assetDetail WHERE category LIKE :category AND date LIKE :date")
    fun findByDetails(category: String, date: String): Flow<List<AssetDetail>>

    @Query("SELECT * FROM assetDetail WHERE date LIKE :date")
    fun findByDateDetails(date: String): Flow<List<AssetDetail>>

    @Delete
    fun delete(searchDetail: AssetDetail?): Int

    @Query("DELETE FROM assetDetail")
    fun clearTable(): Int
}