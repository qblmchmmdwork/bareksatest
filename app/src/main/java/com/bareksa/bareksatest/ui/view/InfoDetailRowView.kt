package com.bareksa.bareksatest.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.bareksa.bareksatest.databinding.InfoDetailRowBinding

class InfoDetailRowView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private val binding: InfoDetailRowBinding

    init {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = InfoDetailRowBinding.inflate(layoutInflater, this)
        setPadding(10)
        orientation = VERTICAL
    }

    var title: String?
        set(value) {
            if (value == null) binding.title.visibility = View.GONE
            else binding.title.apply { visibility = View.VISIBLE; text = value }
        }
        get() = if (binding.title.visibility == View.GONE) null else binding.title.text.toString()

    fun setItems(views: List<View>) {
        views.forEach { view ->
            binding.row.addView(
                view,
                LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                    1f
                ).apply { setMargins(16, 16, 16, 16) }
            )
        }
    }
}