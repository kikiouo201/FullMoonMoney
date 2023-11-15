package com.example.fullmoonmoney.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.fullmoonmoney.data.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao : BaseDao<Project> {
    @Query("SELECT * FROM project")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM project WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Flow<List<Project>>

    @Query("SELECT * FROM project WHERE name LIKE :name LIMIT 1")
    fun findByDetail(name: String): Project

    @Delete
    fun delete(searchCategory: Project?): Int

    @Query("DELETE FROM project")
    fun clearTable(): Int
}