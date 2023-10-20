package com.example.fullmoonmoney.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fullmoonmoney.data.CategoryDetails
import com.example.fullmoonmoney.data.room.RoomConverter
import com.example.fullmoonmoney.data.room.CategoryDetailsDao

@Database(entities = [CategoryDetails::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDetailsDao(): CategoryDetailsDao
}