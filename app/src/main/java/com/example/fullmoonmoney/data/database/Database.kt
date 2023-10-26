package com.example.fullmoonmoney.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fullmoonmoney.data.Category
import com.example.fullmoonmoney.data.CategoryDetails
import com.example.fullmoonmoney.data.room.CategoryDao
import com.example.fullmoonmoney.data.room.RoomConverter
import com.example.fullmoonmoney.data.room.CategoryDetailsDao

@Database(entities = [Category::class, CategoryDetails::class], version = 2, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryDetailsDao(): CategoryDetailsDao
}