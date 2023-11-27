package com.example.fullmoonmoney.ui.custom

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// 標題
@Composable
fun TableHeaderCell(
    @StringRes textList: List<Int>,
    color: Color,
    weight: Float = 1f,
) {
    Row {
        textList.forEach {
            Text(
                text = stringResource(it),
                modifier = Modifier
                    .padding(5.dp)
                    .border(1.dp, Color.Black)
                    .background(color)
                    .weight(weight)
                    .padding(10.dp)
            )
        }
    }
}

// 內容
@Composable
fun TableContentCell(
    text: String,
    textFieldText: String,
    isEdit: Boolean = false,
    onChangeText: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$text :",
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        )
        if (isEdit) {
            OutlinedTextField(
                value = textFieldText,
                onValueChange = { onChangeText(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        } else {
            Text(
                text = textFieldText,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            )
        }
    }
}

// 內容
@Composable
fun EditContentCell(
    title: String,
    price: Int,
    frequency: Int,
    onChangeTitle: (String) -> Unit,
    onChangePrice: (String) -> Unit,
    onChangeFrequency: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = title,
            onValueChange = { onChangeTitle(it) },
            modifier = Modifier
                .padding(5.dp)
                .weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        OutlinedTextField(
            value = price.toString(),
            onValueChange = { onChangePrice(it) },
            modifier = Modifier
                .padding(5.dp)
                .weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = frequency.toString(),
            onValueChange = { onChangeFrequency(it) },
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}