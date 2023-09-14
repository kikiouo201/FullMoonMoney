package com.example.fullmoonmoney.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullmoonmoney.R
import com.example.fullmoonmoney.ui.theme.FullMoonMoneyTheme
import java.text.DecimalFormat

@Composable
fun ProgressIndicator(
    progressTitle: String,
    targetTitle: String,
    progressText: String,
    targetText: String,
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color = colorResource(R.color.blue_750),
    backgroundColor: Color = colorResource(R.color.blue_250),
    clipShape: Shape = RoundedCornerShape(5.dp),
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = progressTitle,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .clip(clipShape)
                .height(50.dp)
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .background(progressColor)
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
            ) {}
            Text(
                text = progressText,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterStart)
            )
            Text(
                text = targetText,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        Text(
            text = targetTitle,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }

}

fun getProgress(present: Int, target: Int): Float =
    if (present == 0 || target == 0)
        0f
    else
        DecimalFormat("#.##").format(present.toFloat() / target).toFloat()

@Preview(showBackground = true)
@Composable
fun LinearProgressIndicatorPreview() {
    FullMoonMoneyTheme {
        ProgressIndicator(
            "淨值 :",
            ": 目標",
            "$5,000",
            "$30,000",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            progress = 0.1f,
        )
    }
}