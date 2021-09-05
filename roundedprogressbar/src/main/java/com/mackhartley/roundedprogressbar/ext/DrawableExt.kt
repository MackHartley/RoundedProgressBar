package com.mackhartley.roundedprogressbar.ext

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun Drawable.setDrawableTint(@ColorInt color: Int) {
    // See: https://stackoverflow.com/questions/11376516/change-drawable-color-programmatically
    val targetDrawableCompat = DrawableCompat.wrap(this)
    DrawableCompat.setTint(targetDrawableCompat, color)
}