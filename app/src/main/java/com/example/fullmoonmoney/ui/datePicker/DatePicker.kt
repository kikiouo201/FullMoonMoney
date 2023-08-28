package com.example.fullmoonmoney.ui.datePicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDropdownMenu(selectedDate: Pair<Int, Int>, onSelected: (Pair<Int, Int>) -> Unit) {
    // TODO 改成只選月日
    var expanded by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    calendar[Calendar.YEAR] = selectedDate.first
    calendar[Calendar.MONTH] = selectedDate.second - 1
    val datePickerState =
        rememberDatePickerState(initialDisplayedMonthMillis = calendar.timeInMillis)

    Box {
        IconButton(modifier = Modifier.width(80.dp),
            onClick = { expanded = true }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${selectedDate.first}/${selectedDate.second}")
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier.fillMaxHeight(),
                    contentDescription = ""
                )
            }
        }
        if (expanded) {
            DatePickerDialog(
                onDismissRequest = { expanded = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            calendar.timeInMillis = it
                        }
                        onSelected(Pair(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1))
                        expanded = false
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { expanded = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthDropdownMenuPreview() {
    FullMoonMoneyTheme {
        MonthDropdownMenu(Pair(2023, 1)) {}
    }
}