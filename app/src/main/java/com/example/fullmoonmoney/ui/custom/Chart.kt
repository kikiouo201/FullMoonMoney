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
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            val pieChart = PieChart(context)
            pieChart.setUsePercentValues(true) // 使用百分比
            pieChart.description.isEnabled = false // 隱藏說明
            pieChart.setEntryLabelColor(textColor.toArgb()) // 圖表內標籤文字的顏色
            pieChart.setEntryLabelTextSize(12f) // 圖表內標籤文字的大小
            pieChart.setHoleColor(R.color.transparent) // 設置孔顏色
            pieChart.holeRadius = 50f // 設置孔半徑
            pieChart.transparentCircleRadius = 0f  // 設置孔半透明環的寬度
            pieChart.centerText = "淨值\n${total}" // 設置孔中的文字
            pieChart.setCenterTextColor(textColor.toArgb()) // 設置孔中的文字顏色
            pieChart.legend.textColor = textColor.toArgb() // 標籤文字的顏色

            val entries: ArrayList<PieEntry> = ArrayList()
            entries.add(PieEntry(60f, context.getString(R.string.income)))
            entries.add(PieEntry(20f, context.getString(R.string.invest)))
            entries.add(PieEntry(10f, context.getString(R.string.expenditure)))
            entries.add(PieEntry(10f, context.getString(R.string.debt)))

            val dataSet = PieDataSet(entries, context.getString(R.string.monthly_accounting))
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 2f // 餅狀項目之間的間距
            dataSet.selectionShift = 10f // 餅狀項目選擇時放大的距離
            dataSet.colors = listOf(
                ContextCompat.getColor(context, R.color.orange_200),
                ContextCompat.getColor(context, R.color.purple_200),
                ContextCompat.getColor(context, R.color.teal_700),
                ContextCompat.getColor(context, R.color.blue_200),
            )

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(12f)
            data.setValueTextColor(textColor.toArgb())
            data.setDrawValues(true)

            pieChart.data = data
            pieChart.invalidate()

            pieChart
        }
    ) {
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
            val legends = listOf("預算", "實際花費")
            val labels = listOf(
                context.getString(R.string.income),
                context.getString(R.string.invest),
                context.getString(R.string.expenditure),
                context.getString(R.string.debt)
            )

            val barChart = BarChart(context)
            barChart.setBackgroundColor(Color.Transparent.toArgb())
            barChart.description.isEnabled = false
            barChart.axisLeft.setDrawAxisLine(false)
            barChart.axisLeft.isEnabled = false
            barChart.axisRight.setDrawAxisLine(false)
            barChart.axisRight.isEnabled = false
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                labelCount = data.size
                valueFormatter = IndexAxisValueFormatter(labels)
                this.textColor = textColor.toArgb()
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            val legendEntry = mutableListOf<LegendEntry>()
            for (i in legends.indices) {
                val entry = LegendEntry()
                entry.formColor = barDataColors[i]
                entry.label = legends[i]
                legendEntry.add(entry)
            }
            barChart.legend.setCustom(legendEntry)
            barChart.legend.textColor = textColor.toArgb()

            val chartData = setChartData(data)
            val barData = BarData()


            chartData.forEach {
                barData.addDataSet(BarDataSet(it, "Label").apply {
                    colors = barDataColors
                    valueTextColor = textColor.toArgb()
                })
            }

            barChart.data = barData
            barChart.invalidate()

            barChart
        }) {
    }
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