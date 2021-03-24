package com.mackhartley.roundedprogressbar.utils

/**
 * Ensures there isn't a corner radius below 0 or above 90 degrees (which would look very weird)
 */
fun calculateAppropriateCornerRadius(requestedRadius: Float, viewHeight: Int): Float {
    val maximumAllowedCornerRadius = viewHeight / 2f // This would be a corner radius of 90 degrees
    return when {
        requestedRadius < 0 -> 0f
        requestedRadius > maximumAllowedCornerRadius -> maximumAllowedCornerRadius
        else -> requestedRadius
    }
}