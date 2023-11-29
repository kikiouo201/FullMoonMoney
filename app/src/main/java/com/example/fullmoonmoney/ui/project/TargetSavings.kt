package com.example.fullmoonmoney.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

@Composable
fun TargetSavings() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = stringResource(id = R.string.target_savings),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Text(text = "選定儲蓄：")
        Text(
            text = "結婚費用",
            modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)
        )
        Text(text = "＊ 金額：40000")
        Text(text = "＊ 2000*10年")
        Text(text = "＊ 4000*10年")
        Text(text = "＊ 目標在29歲完成")
        Text(
            text = "家庭緊急預備金",
            modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)
        )
        Text(text = "＊ 金額：24000")
        Text(text = "＊ 2000*5年")
        Text(text = "＊ 6000*1年")
        Text(text = "＊ 目標在35歲完成")
    }
}

@Preview(showBackground = true)
@Composable
fun TargetSavingsPreview() {
    FullMoonMoneyTheme {
        TargetSavings()
    }
}