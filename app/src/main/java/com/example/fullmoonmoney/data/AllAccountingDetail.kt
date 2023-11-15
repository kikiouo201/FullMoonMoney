package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.AccountingDetailDao
import com.example.fullmoonmoney.ui.generalAccounting.AccountingCategory
import kotlinx.coroutines.flow.Flow

class AllAccountingDetail(
    private val accountingDetailDao: AccountingDetailDao,
) {
    suspend fun addDetail(detail: AccountingDetail) {
        accountingDetailDao.insert(detail)
    }

    suspend fun addDetailList(detail: List<AccountingDetail>) {
        accountingDetailDao.insertAll(detail)
    }

    fun getDetailList(category: AccountingCategory, date: String): Flow<List<AccountingDetail>> {
        return accountingDetailDao.findByDetailList(category.name, date)
    }

    fun getDateDetailList(date: String): Flow<List<AccountingDetail>> {
        return accountingDetailDao.findByDateDetailList(date)
    }
}