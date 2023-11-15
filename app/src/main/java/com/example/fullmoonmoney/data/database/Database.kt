package com.example.fullmoonmoney.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fullmoonmoney.data.AccountingDetail
import com.example.fullmoonmoney.data.AssetDetail
import com.example.fullmoonmoney.data.Project
import com.example.fullmoonmoney.data.CategoryDetail
import com.example.fullmoonmoney.data.room.AccountingDetailDao
import com.example.fullmoonmoney.data.room.AssetDetailDao
import com.example.fullmoonmoney.data.room.ProjectDao
import com.example.fullmoonmoney.data.room.RoomConverter
import com.example.fullmoonmoney.data.room.CategoryDetailDao

@Database(
    entities = [Project::class, CategoryDetail::class, AssetDetail::class, AccountingDetail::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun categoryDetailsDao(): CategoryDetailDao
    abstract fun assetDetailsDao(): AssetDetailDao
    abstract fun accountingDetailDao(): AccountingDetailDao
}