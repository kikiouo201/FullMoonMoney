package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.CategoryDetails

@Dao
interface CategoryDetailsDao : BaseDao<CategoryDetails> {
    @Query("SELECT * FROM categoryDetails")
    fun getAll(): List<CategoryDetails>

    @Query("SELECT * FROM categoryDetails WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<CategoryDetails>

    @Query("SELECT * FROM categoryDetails WHERE category LIKE :category AND date LIKE :date LIMIT 1")
    fun findByDetail(category: String, date: String): CategoryDetails

    @Delete
    fun delete(searchDetail: CategoryDetails?): Int

    @Query("DELETE FROM categoryDetails")
    fun clearTable(): Int
}