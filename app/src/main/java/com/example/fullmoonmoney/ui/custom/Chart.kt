package com.example.fullmoonmoney.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun MonthPieChart(modifier: Modifier, textColor: Color, total: String) {
    AndroidView(modifier = modifier.fillMaxWidth(), factory = { context ->
        val pieChart = PieChart(context).apply {
            setUsePercentValues(true) // 使用百分比
            description.isEnabled = false // 隱藏說明
            setEntryLabelColor(textColor.toArgb()) // 圖表內標籤文字的顏色
            setEntryLabelTextSize(12f) // 圖表內標籤文字的大小
            setHoleColor(R.color.transparent) // 設置孔顏色
            holeRadius = 50f // 設置孔半徑
            transparentCircleRadius = 0f  // 設置孔半透明環的寬度
            centerText = "淨值\n${total}" // 設置孔中的文字
            setCenterTextColor(textColor.toArgb()) // 設置孔中的文字顏色
            legend.textColor = textColor.toArgb() // 標籤文字的顏色
        }
        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(60f, context.getString(R.string.income)))
            add(PieEntry(20f, context.getString(R.string.invest)))
            add(PieEntry(10f, context.getString(R.string.expenditure)))
            add(PieEntry(10f, context.getString(R.string.debt)))
        }
        val dataSet = PieDataSet(entries, context.getString(R.string.monthly_accounting)).apply {
            setDrawIcons(false)
            sliceSpace = 2f // 餅狀項目之間的間距
            selectionShift = 10f // 餅狀項目選擇時放大的距離
            colors = listOf(
                ContextCompat.getColor(context, R.color.orange_200),
                ContextCompat.getColor(context, R.color.purple_200),
                ContextCompat.getColor(context, R.color.teal_700),
                ContextCompat.getColor(context, R.color.blue_200),
            )
        }

        pieChart.data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(12f)
            setValueTextColor(textColor.toArgb())
            setDrawValues(true)
        }
        pieChart.invalidate()

        pieChart
    }) {
        it.centerText = "淨值\n${total}"
        it.notifyDataSetChanged()
        it.invalidate()
    }
}

@Composable
fun MonthlyAccountingBarChart(modifier: Modifier, textColor: Color) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val legends = listOf("預算", "實際花費")
            val barDataColors = listOf(
                ContextCompat.getColor(context, R.color.blue_200),
                ContextCompat.getColor(context, R.color.yellow_200)
            )
            val data = listOf(
                listOf(3000f, 2000f),
                listOf(5000f, 2000f),
                listOf(4000f, 3000f),
                listOf(5000f, 4000f)
            )
            val barChart = BarChart(context).apply {
                setBackgroundColor(Color.Transparent.toArgb())
                description.isEnabled = false
                axisLeft.setDrawAxisLine(false)
                axisLeft.isEnabled = false
                axisRight.setDrawAxisLine(false)
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelCount = data.size
                xAxis.valueFormatter = IndexAxisValueFormatter(
                    listOf(
                        context.getString(R.string.income),
                        context.getString(R.string.invest),
                        context.getString(R.string.expenditure),
                        context.getString(R.string.debt)
                    )
                )
                xAxis.textColor = textColor.toArgb()
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)
                legend.setCustom(getLegendEntry(legends, barDataColors))
                legend.textColor = textColor.toArgb()
            }

            barChart.data = BarData().apply {
                getBarEntry(data).forEach {
                    this.addDataSet(BarDataSet(it, "Label").apply {
                        colors = barDataColors
                        valueTextColor = textColor.toArgb()
                    })
                }
            }
            barChart.invalidate()

            barChart
        }) {}
}

@Composable
fun MonthBarChart(modifier: Modifier, textColor: Color, data: List<List<Float>>) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val barDataColors = listOf(
                ContextCompat.getColor(context, R.color.blue_200),
                ContextCompat.getColor(context, R.color.yellow_200)
            )

            val barChart = BarChart(context).apply {
                setBackgroundColor(Color.Transparent.toArgb())
                axisLeft.setDrawAxisLine(false)
                axisLeft.isEnabled = false
                axisRight.setDrawAxisLine(false)
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelCount = 12
                xAxis.textColor = textColor.toArgb()
                xAxis.valueFormatter = IndexAxisValueFormatter(getMonthString())
            }

            barChart.data = BarData().apply {
                getBarEntry(data).forEach {
                    this.addDataSet(BarDataSet(it, "Label").apply {
                        colors = barDataColors
                        valueTextColor = textColor.toArgb()
                    })
                }
            }
            barChart.invalidate()

            barChart
        }) {}
}

fun getBarEntry(data: List<List<Float>>): MutableList<MutableList<BarEntry>> {
    return mutableListOf<MutableList<BarEntry>>().apply {
        data.forEachIndexed { index, floats ->
            this.add(
                index,
                mutableListOf<BarEntry>().also {
                    floats.forEach { float ->
                        it.add(BarEntry(index.toFloat(), float))
                    }
                }
            )
        }
    }
}

fun getLegendEntry(legends: List<String>, barDataColors: List<Int>): MutableList<LegendEntry> {
    return mutableListOf<LegendEntry>().apply {
        barDataColors.forEachIndexed { index, barDataColor ->
            this.add(
                LegendEntry().apply {
                    formColor = barDataColor
                    label = legends.elementAtOrNull(index) ?: ""
                }
            )
        }
    }
}

fun getMonthString(): List<String> {
    val monthString = mutableListOf<String>()
    for (i in 1..12) {
        monthString.add("${i}月")
    }
    return monthString
}

@Preview(showBackground = true)
@Composable
fun MonthBarChartPreview() {
    FullMoonMoneyTheme {
        MonthBarChart(
            Modifier.height(100.dp),
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

@Preview(showBackground = true)
@Composable
fun MonthlyChartPreview() {
    FullMoonMoneyTheme {
        MonthlyAccountingBarChart(
            Modifier.height(300.dp),
            MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    FullMoonMoneyTheme {
        MonthPieChart(
            Modifier.height(300.dp),
            MaterialTheme.colorScheme.onPrimary,
            "9,992"
        )
    }
}