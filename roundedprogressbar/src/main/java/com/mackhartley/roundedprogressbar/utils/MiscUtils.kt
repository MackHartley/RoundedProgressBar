package com.mackhartley.roundedprogressbar.utils

import kotlin.math.roundToInt

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

/**
 * Takes the [completionRatio] float and turns it into an integer string (eg 0.415f -> "42%")
 *
 * @param onlyShowTrue0 If this is true then 0% will only ever be shown if the [completionRatio] is
 * actually 0. This means values like 0.2 will be shown as 1%
 * @param onlyShowTrue100 If this is true then 100% will only ever be shown if the [completionRatio]
 * is actually 100. This means values like 99.8 will be shown as 99%
 */
fun getPercentageString(
    completionRatio: Float,
    onlyShowTrue0: Boolean,
    onlyShowTrue100: Boolean
): String {
    val percentage = completionRatio * 100

    val intValue: Int = when {
        percentage > 0f && percentage < 1f -> {
            if (onlyShowTrue0) 1
            else percentage.roundToInt()
        }
        percentage > 99f && percentage < 100f -> {
            if (onlyShowTrue100) 99
            else percentage.roundToInt()
        }
        else -> percentage.roundToInt()
    }

    return "$intValue%"
}