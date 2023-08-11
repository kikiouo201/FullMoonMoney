package com.example.fullmoonmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fullmoonmoney.ui.home.Home
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FullMoonMoneyTheme {
                Home()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FullMoonMoneyTheme {
        Home()
    }
}