package com.bareksa.bareksatest.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.databinding.InfoDetailRowItemBinding

class InfoDetailRowItemView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val binding: InfoDetailRowItemBinding

    init {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        orientation = VERTICAL
        binding = InfoDetailRowItemBinding.inflate(layoutInflater, this)
        background = ContextCompat.getDrawable(context, R.drawable.rounded_corner)
        setPadding((8 * resources.displayMetrics.density).toInt())
        binding.image.visibility = View.GONE
    }

    var label: String
        set(value) {
            binding.label.text = value
        }
        get() = binding.label.text.toString()


    fun setImage(@DrawableRes drawableRes: Int? = null) {
        if(drawableRes == null) binding.image.visibility = View.GONE
        else binding.image.apply {
            this@InfoDetailRowItemView.setPadding((14 * resources.displayMetrics.density).toInt())
            visibility = View.VISIBLE
            setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        }
    }

    fun setBackgroundTintColor(@ColorRes backgroundColor: Int) {
        background.setTint(ContextCompat.getColor(context, backgroundColor))
    }
}