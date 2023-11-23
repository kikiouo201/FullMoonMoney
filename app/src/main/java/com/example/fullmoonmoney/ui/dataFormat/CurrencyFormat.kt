package com.example.fullmoonmoney.ui.dataFormat

import java.text.DecimalFormat

fun formatCurrency(money: Int, isNegative: Boolean = false) =
    "$${DecimalFormat((if (isNegative) "-" else "") + "#,###,###,###,###,###,###").format(money)}"