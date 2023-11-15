package com.example.fullmoonmoney.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 資產類別明細
@Entity(tableName = "AssetDetail")
data class AssetDetail(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "index") val index: Int = 0,
    @ColumnInfo(name = "category") val category: String,  //  分類
    @ColumnInfo(name = "date") val date: String,  //  時間
    @ColumnInfo(name = "item") val item: String,  //  項目名稱
    @ColumnInfo(name = "amount") var amount: String = "0",  //  金額
)