package com.example.fullmoonmoney

import android.content.Context
import androidx.room.Room
import com.example.fullmoonmoney.data.AllAssetDetail
import com.example.fullmoonmoney.data.AllCategory
import com.example.fullmoonmoney.data.AllCategoryDetail
import com.example.fullmoonmoney.data.database.AppDatabase

object Graph {

    private lateinit var database: AppDatabase

    val allAssetDetailDao by lazy {
        AllAssetDetail(
            assetDetailDao = database.assetDetailsDao()
        )
    }

    val allCategoryDetailDao by lazy {
        AllCategoryDetail(
            categoryDetailDao = database.categoryDetailsDao()
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