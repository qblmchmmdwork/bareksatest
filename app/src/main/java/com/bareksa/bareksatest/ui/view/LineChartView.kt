package com.bareksa.bareksatest.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.ui.grafik.GrafikFragment
import com.bareksa.bareksatest.util.HighlightedCircleLineChartRenderer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class LineChartView(context: Context, attrs: AttributeSet? = null) : LineChart(context, attrs) {

    private val dateFormat = SimpleDateFormat("MMM dd", Locale("id", "ID"))

    private var legendView: LegendView? = null

    private var circleRadius = 6f
    private var circleHoleRadius = 4f

    private val circleBitmaps = hashMapOf<String, Bitmap>()
    private val tf = ResourcesCompat.getFont(context, R.font.montserrat_regular).run {
        Typeface.create(this, 400, false)
    }
    private var grafikData: List<GrafikFragment.GrafikData>? = null

    init {
        legend.isEnabled = false
        extraBottomOffset = 10f
        isDoubleTapToZoomEnabled = false
        setPinchZoom(false)
        setScaleEnabled(false)
        description.isEnabled = false
        xAxis.apply {
            labelCount = 5
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setAvoidFirstLastClipping(true)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dateFormat.format(grafikData!!.first().entries[value.toInt()].date)
                }
            }
            this.yOffset = 8f
            textSize = 10f
            typeface = tf
        }
        axisRight.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            this.xOffset = 16f
            axisMinimum = 0f
            labelCount = 5
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value.rem(1).equals(0.0f)) String.format("%.0f%%", value)
                    else String.format("%.2f%%", value)
                }
            }
            typeface = tf
        }
        axisLeft.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            gridColor = Color.parseColor("#33000000")
            labelCount = 9
            axisMinimum = 0f
        }
        renderer = HighlightedCircleLineChartRenderer(
            this,
            animator,
            viewPortHandler,
            circleBitmaps
        )

    }

    fun setLegend(legendView: LegendView) {
        this.legendView = legendView
        grafikData?.let { legendView.setData(it, circleBitmaps) }
        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight?) {
                legendView.update(data.dataSets.map { it.getEntryForXValue(e.x, e.y) })
            }

            override fun onNothingSelected() {}
        })
    }

    fun setData(grafikData: List<GrafikFragment.GrafikData>) {
        if (grafikData.isEmpty()) return
        this.grafikData = grafikData
        val lineDataSets = grafikData.map { data ->
            circleBitmaps[data.name] = createCircleBitmap(
                ContextCompat.getColor(context, data.color),
                ContextCompat.getColor(context, R.color.navy_50)
            )
            LineDataSet(
                data.entries.mapIndexed { a, grafikEntry -> Entry(a.toFloat(), grafikEntry.value) },
                data.name
            ).apply {
                circleRadius = this@LineChartView.circleRadius
                lineWidth = 2f
                highLightColor = ContextCompat.getColor(context, R.color.disabled)
                setDrawHorizontalHighlightIndicator(false)
                color = ContextCompat.getColor(context, data.color)
            }
        }
        data = LineData(lineDataSets)
        legendView?.setData(grafikData, circleBitmaps)
        highlightValue(grafikData[0].entries.lastIndex.toFloat(), 0)
    }

    private fun createCircleBitmap(
        circleColor: Int,
        holeColor: Int,
    ): Bitmap {
        val circleRadius = circleRadius * resources.displayMetrics.density
        val circleHoleRadius = circleHoleRadius * resources.displayMetrics.density
        val circlePainter = Paint().apply { color = circleColor }
        val holePainter = Paint().apply { color = holeColor }

        val circleBitmap = Bitmap.createBitmap(
            (circleRadius * 2.1).toInt(),
            (circleRadius * 2.1).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(circleBitmap)
        canvas.drawCircle(
            circleRadius,
            circleRadius,
            circleRadius,
            circlePainter
        )
        canvas.drawCircle(
            circleRadius,
            circleRadius,
            circleHoleRadius,
            holePainter
        )
        return circleBitmap
    }
}