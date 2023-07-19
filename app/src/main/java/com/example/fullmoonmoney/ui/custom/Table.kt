package com.example.fullmoonmoney.ui.custom

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

// 標題
@Composable
fun TableHeaderCell(
    @StringRes textList: List<Int>,
    color: Color,
    weight: Float = 1f
) {
    Row(Modifier.background(color)) {
        textList.forEach {
            Text(
                text = stringResource(it),
                modifier = Modifier
                    .border(1.dp, Color.Black)
                    .weight(weight)
                    .padding(10.dp)
            )
        }
    }
}

// 內容
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableContentCell(
    text: String,
    textFieldText: String,
    weight: Float = 1f
) {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(textFieldText, TextRange(0, 7)))
    }
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$text :",
            Modifier
                .weight(weight)
                .padding(10.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier
                .weight(weight)
                .padding(5.dp)
        )
    }
}

