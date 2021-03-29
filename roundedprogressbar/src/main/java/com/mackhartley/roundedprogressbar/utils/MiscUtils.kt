package com.mackhartley.roundedprogressbar.utils

/**
 * Ensures corner radius is above 0 and doesn't exceed viewHeight/2. This prevents a rounded corner
 * from affecting the area of a different rounded corner (which might look weird)
 *
 * Except - If "isMaxRadiusRestricted = false" then any corner radius above 0 can be set. This allows
 * for greater customization but can lead to weird looking progress bars.
 */
fun calculateAppropriateCornerRadius(
    requestedRadius: Float,
    viewHeight: Int,
    isRadiusRestricted: Boolean
): Float {
    val maximumAllowedCornerRadius = viewHeight / 2f // This would be a corner radius of 90 degrees
    return when {
        requestedRadius < 0 -> 0f
        !isRadiusRestricted -> requestedRadius
        requestedRadius > maximumAllowedCornerRadius -> maximumAllowedCornerRadius
        else -> requestedRadius
    }
}