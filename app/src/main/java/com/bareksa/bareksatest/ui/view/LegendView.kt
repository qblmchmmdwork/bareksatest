package com.bareksa.bareksatest.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.setPadding
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.ui.grafik.GrafikFragment
import com.github.mikephil.charting.data.Entry
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import java.text.SimpleDateFormat
import java.util.*

class LegendView(context: Context, attrs: AttributeSet? = null) : FlexboxLayout(context, attrs) {
    private val legendDateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    private var circleBitmaps : HashMap<String, Bitmap>? = null
    private var grafikData: List<GrafikFragment.GrafikData>? = null


    private var itemTextViews: List<TextView>? = null
    private var dateTextView: TextView? = null

    init {
        flexWrap = FlexWrap.WRAP
        alignItems = AlignItems.CENTER
        alignContent = AlignContent.CENTER
    }

    fun setData(grafikData: List<GrafikFragment.GrafikData>, circleBitmaps: HashMap<String, Bitmap>) {
        removeAllViews()
        this.grafikData = grafikData
        this.circleBitmaps = circleBitmaps
        itemTextViews = grafikData.map {
            TextView(context).apply {
                text = it.entries.last().value.toString()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                gravity = Gravity.CENTER
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    circleBitmaps[it.name]!!.toDrawable(resources),
                    null,
                    null,
                    null
                )
                typeface = Typeface.create(typeface, 500, false)
                setTextColor(Color.parseColor("#EE000000"))
                val dp4 = (4 * resources.displayMetrics.density).toInt()
                compoundDrawablePadding = dp4
                setPadding(dp4 )
                addView(this)
            }
        }
        addView(TextView(context).apply {
            text = legendDateFormatter.format(grafikData.first().entries.last().date)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            gravity = Gravity.CENTER
            setPadding((2 * resources.displayMetrics.density).toInt())
            dateTextView = this
        })
    }

    fun update(entries: List<Entry>) {
        if (entries.isEmpty()) return
        for (i in entries.indices) {
            val textView = itemTextViews?.getOrNull(i) ?: break
            val entry = entries[i]
            textView.text = String.format("%.2f%%", entry.y)
        }
        val date =
            grafikData?.firstOrNull()?.entries?.getOrNull(entries.first().x.toInt())?.date ?: return
        dateTextView?.text = resources.getString(R.string.in_parenthesis_text,legendDateFormatter.format(date))
    }
}