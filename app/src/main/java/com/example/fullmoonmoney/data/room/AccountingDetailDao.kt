package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.AccountingDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountingDetailDao : BaseDao<AccountingDetail> {
    @Query("SELECT * FROM accountingDetail")
    fun getAll(): List<AccountingDetail>

    @Query("SELECT * FROM accountingDetail WHERE date LIKE :date")
    fun findByDateDetailList(date: String): Flow<List<AccountingDetail>>

    @Delete
    fun delete(searchDetail: AccountingDetail?): Int

    @Query("DELETE FROM accountingDetail")
    fun clearTable(): Int
}