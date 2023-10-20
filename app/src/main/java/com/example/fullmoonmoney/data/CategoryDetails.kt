package com.example.fullmoonmoney.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = ["category", "date"],
        unique = true
    )]
)
data class CategoryDetails(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "category") val category: String,  //  分類
    @ColumnInfo(name = "date") val date: String,  //  時間
    @ColumnInfo(name = "project") val project: String?, //  專案
    @ColumnInfo(name = "details") val details: Map<String, String>?, //  明細 （項目名稱、金額）
)