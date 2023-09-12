package com.example.fullmoonmoney.ui.dataFormat

import java.text.DecimalFormat

fun formatCurrency(money: Int): String {
    val decimalFormat = DecimalFormat("#,###")
    return "$${decimalFormat.format(money)}"
}