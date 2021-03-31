package com.bareksa.bareksatest.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.*

class HighlightedCircleLineChartRenderer(
    chart: LineDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val circleBitmaps : HashMap<String, Bitmap>
) : LineChartRenderer(chart, animator, viewPortHandler) {

    private var highlighted: Highlight? = null

    override fun drawHighlighted(c: Canvas, indices: Array<out Highlight>) {
        super.drawHighlighted(c, indices)
        highlighted = indices.first()
    }


    override fun drawCircles(canvas: Canvas) {
        mRenderPaint.style = Paint.Style.FILL
        val phaseY = mAnimator.phaseY
        val dataSets = mChart.lineData.dataSets
        val highlighted = highlighted ?: return
        for (dataSet in dataSets) {
            val trans = mChart.getTransformer(dataSet.axisDependency)
            val highlightedEntry = dataSet.getEntryForXValue(highlighted.x, highlighted.y)
            val (x, y) = floatArrayOf(highlightedEntry.x, highlightedEntry.y * phaseY).apply {
                trans.pointValuesToPixel(this)
            }
            val circleRadius = dataSet.circleRadius
            val circleBitmap: Bitmap = circleBitmaps[dataSet.label]!!
            canvas.drawBitmap(
                circleBitmap,
                x - circleRadius,
                y - circleRadius,
                null
            )
        }
    }


}