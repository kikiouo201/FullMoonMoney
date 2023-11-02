package com.example.fullmoonmoney.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["category", "date", "item"]
)
data class AssetDetail(
    @ColumnInfo(name = "category") val category: String,  //  分類
    @ColumnInfo(name = "date") val date: String,  //  時間
    @ColumnInfo(name = "item") val item: String,  //  項目名稱
    @ColumnInfo(name = "amount") val amount: String = "0",  //  金額
)