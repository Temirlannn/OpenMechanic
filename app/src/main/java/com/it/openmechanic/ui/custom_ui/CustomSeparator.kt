package com.it.openmechanic.ui.custom_ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.it.openmechanic.R

class CustomSeparator(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SeparatorView,
            0,
            R.style.SeparatorStyle
        ).apply {
            try {
                setBackgroundColor(getColor(R.styleable.SeparatorView_color, 0))
                alpha = getFloat(R.styleable.SeparatorView_alpha, 1f)
            } finally {
                recycle()
            }
        }
    }
}