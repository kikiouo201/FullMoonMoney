package com.example.fullmoonmoney.ui.custom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun MyLineChart(modifier: Modifier, textColor: Color, data: List<List<Float>>) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val barDataColors = listOf(
                ContextCompat.getColor(context, R.color.blue_200),
                ContextCompat.getColor(context, R.color.yellow_200)
            )
            val lineChart = BarChart(context)
            lineChart.setBackgroundColor(Color.Transparent.toArgb())
            lineChart.axisLeft.setDrawAxisLine(false)
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.setDrawAxisLine(false)
            lineChart.axisRight.isEnabled = false
            lineChart.description.isEnabled = false
            lineChart.legend.isEnabled = false
            lineChart.xAxis.setDrawAxisLine(false)
            lineChart.xAxis.setDrawGridLines(false)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.labelCount = 12
            lineChart.xAxis.textColor = textColor.toArgb()
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(getMonthString())

            val chartData = setChartData(data)
            val lineData = BarData()
            chartData.forEach {
                lineData.addDataSet(BarDataSet(it, "Label").apply {
                    colors = barDataColors
                    valueTextColor = textColor.toArgb()
                })
            }

            lineChart.data = lineData
            lineChart.invalidate()

            lineChart
        }) {
    }
}

fun setChartData(data: List<List<Float>>): MutableList<MutableList<BarEntry>> {
    val allEntries = mutableListOf<MutableList<BarEntry>>()
    data.forEachIndexed { index, floats ->
        val barEntry = mutableListOf<BarEntry>()
        floats.forEach {
            barEntry.add(BarEntry(index.toFloat(), it))
        }
        allEntries.add(index, barEntry)
    }

    return allEntries
}

fun getMonthString(): List<String> {
    val monthString = mutableListOf<String>()
    for (i in 1..12) {
        monthString.add("${i}月")
    }
    return monthString
}

@Preview
@Composable
fun LineChartPreview() {
    FullMoonMoneyTheme {
        MyLineChart(
            Modifier.fillMaxSize(),
            MaterialTheme.colorScheme.onPrimary,
            listOf(
                listOf(3000f, 2000f),
                listOf(5000f, 2000f),
                listOf(4000f, 3000f),
                listOf(5000f, 4000f),
                listOf(5000f, 3000f),
                listOf(5000f, 4000f),
                listOf(5000f, 2000f),
                listOf(4000f, 4000f),
                listOf(6000f, 2000f),
                listOf(4000f, 3000f),
                listOf(5000f, 2000f),
                listOf(5000f, 1000f)
            )
        )
    }
}