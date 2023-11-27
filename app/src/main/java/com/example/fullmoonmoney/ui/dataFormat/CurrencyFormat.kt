package com.example.fullmoonmoney.ui.dataFormat

import java.text.DecimalFormat

fun formatCurrency(money: Int, isNegative: Boolean = false) =
    if (isNegative) formatNegative(money) else formatPositive(money)

fun formatDecimal(money: Double) =
    "$${DecimalFormat("#,###,###,###,###,###,###.##").format(money)}"

fun formatPercentage(molecular: Int, denominator: Int) =
    "${(molecular.toFloat() / denominator.toFloat() * 100).toInt()}%"

private fun formatPositive(money: Int) =
    "$${DecimalFormat("#,###,###,###,###,###,###").format(money)}"

private fun formatNegative(money: Int) =
    "$${DecimalFormat("-#,###,###,###,###,###,###").format(money)}"
