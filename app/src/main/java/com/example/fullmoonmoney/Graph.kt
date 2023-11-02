package com.example.fullmoonmoney

import android.content.Context
import androidx.room.Room
import com.example.fullmoonmoney.data.AllAssetDetails
import com.example.fullmoonmoney.data.AllCategory
import com.example.fullmoonmoney.data.AllCategoryDetails
import com.example.fullmoonmoney.data.database.AppDatabase

object Graph {

    private lateinit var database: AppDatabase

    val allAssetDetailsDao by lazy {
        AllAssetDetails(
            assetDetailsDao = database.assetDetailsDao()
        )
    }

    val allCategoryDetailsDao by lazy {
        AllCategoryDetails(
            categoryDetailsDao = database.categoryDetailsDao()
        )
    }

    val allCategoryDao by lazy {
        AllCategory(
            categoryDao = database.categoryDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}