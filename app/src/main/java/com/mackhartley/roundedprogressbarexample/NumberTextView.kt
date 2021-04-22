package com.mackhartley.roundedprogressbarexample

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NumberTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    fun setIntVal(value: Int) {
        val str = value.toString()
        setText(str)
    }
}