package com.example.fullmoonmoney.ui.dataFormat

import java.text.DecimalFormat

fun formatCurrency(money: Int) = "$${DecimalFormat("#,###").format(money)}"