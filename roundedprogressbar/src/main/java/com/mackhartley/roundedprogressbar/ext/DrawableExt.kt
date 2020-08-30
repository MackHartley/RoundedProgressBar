package com.mackhartley.roundedprogressbar.ext

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat

fun Drawable.setColorFilterCompat(context: Context, colorRes: Int) {
    val compatColor = ContextCompat.getColor(context, colorRes)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.colorFilter = BlendModeColorFilter(compatColor, BlendMode.SRC_ATOP)
    } else {
        @Suppress("DEPRECATION")
        this.setColorFilter(compatColor, PorterDuff.Mode.SRC_ATOP)
    }
}