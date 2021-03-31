package com.bareksa.bareksatest.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bareksa.bareksatest.databinding.DetailBeliButtonBinding

class DetailBeliButtonView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val binding: DetailBeliButtonBinding

    init {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        orientation = VERTICAL
        binding = DetailBeliButtonBinding.inflate(layoutInflater, this)
    }

    fun setOnDetailClick(listener: (View)->Unit) {
        binding.detailButton.setOnClickListener(listener)
    }
    fun setOnBeliClick(listener: (View)->Unit) {
        binding.beliButton.setOnClickListener(listener)
    }
}