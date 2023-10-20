package com.example.fullmoonmoney

import android.content.Context
import androidx.room.Room
import com.example.fullmoonmoney.data.AllCategoryDetails
import com.example.fullmoonmoney.data.database.AppDatabase

object Graph {

    private lateinit var database: AppDatabase

    val allCategoryDetailsDao by lazy {
        AllCategoryDetails(
            categoryDetailsDao = database.categoryDetailsDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}