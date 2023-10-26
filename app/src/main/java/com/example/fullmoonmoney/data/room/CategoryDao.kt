package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.Category

@Dao
interface CategoryDao : BaseDao<Category> {
    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<Category>

    @Query("SELECT * FROM category WHERE name LIKE :name LIMIT 1")
    fun findByDetail(name: String): Category

    @Delete
    fun delete(searchCategory: Category?): Int

    @Query("DELETE FROM category")
    fun clearTable(): Int
}