package com.mackhartley.roundedprogressbar.ext

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt

fun Drawable.setColorFilterCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        @Suppress("DEPRECATION")
        this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}