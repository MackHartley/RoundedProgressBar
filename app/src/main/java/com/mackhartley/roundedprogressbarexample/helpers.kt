package com.mackhartley.roundedprogressbarexample

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt

fun colorIntToHexString(@ColorInt color: Int): String {
    return String.format("#%06X", 0xFFFFFF and color)
}

fun convertDpToPix(dp: Float, resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

fun convertSpToPix(sp: Float, resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    )
}