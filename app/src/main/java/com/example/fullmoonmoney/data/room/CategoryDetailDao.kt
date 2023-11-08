package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.CategoryDetail

@Dao
interface CategoryDetailDao : BaseDao<CategoryDetail> {
    @Query("SELECT * FROM categoryDetail")
    fun getAll(): List<CategoryDetail>

    @Query("SELECT * FROM categoryDetail WHERE category LIKE :category")
    fun findByDetailList(category: String): List<CategoryDetail>

    @Delete
    fun delete(searchDetail: CategoryDetail?): Int

    @Query("DELETE FROM categoryDetail")
    fun clearTable(): Int
}