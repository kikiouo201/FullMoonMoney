package com.example.fullmoonmoney.data

import com.example.fullmoonmoney.data.room.ProjectDao
import kotlinx.coroutines.flow.Flow

class AllProject(
    private val projectDao: ProjectDao,
) {
    suspend fun addProject(category: Project) {
        projectDao.insert(category)
    }

    suspend fun addAllProject(categoryList: List<Project>) {
        projectDao.insertAll(categoryList)
    }

    fun getAllProject(): Flow<List<Project>> {
        return projectDao.getAll()
    }
}