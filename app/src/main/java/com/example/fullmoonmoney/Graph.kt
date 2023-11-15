package com.example.fullmoonmoney

import android.content.Context
import androidx.room.Room
import com.example.fullmoonmoney.data.AllAccountingDetail
import com.example.fullmoonmoney.data.AllAssetDetail
import com.example.fullmoonmoney.data.AllProject
import com.example.fullmoonmoney.data.AllCategoryDetail
import com.example.fullmoonmoney.data.database.AppDatabase

object Graph {

    private lateinit var database: AppDatabase

    val allAssetDetailDao by lazy {
        AllAssetDetail(
            assetDetailDao = database.assetDetailsDao()
        )
    }

    val allAccountingDetailDao by lazy {
        AllAccountingDetail(
            accountingDetailDao = database.accountingDetailDao()
        )
    }

    val allCategoryDetailDao by lazy {
        AllCategoryDetail(
            categoryDetailDao = database.categoryDetailsDao()
        )
    }

    val allProjectDao by lazy {
        AllProject(
            projectDao = database.projectDao()
        )
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, "data.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}