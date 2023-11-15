package com.example.fullmoonmoney.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 一般記帳的明細
@Entity(tableName = "AccountingDetail")
data class AccountingDetail(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "index") val index: Int = 0,
    @ColumnInfo(name = "category") val category: String,  // 分類
    @ColumnInfo(name = "date") val date: String,  // 時間
    @ColumnInfo(name = "item") var item: String,  // 項目名稱
    @ColumnInfo(name = "amount") var amount: Int = 0,  // 金額
    @ColumnInfo(name = "projectList") var projectList: MutableList<String>, // 專案
)