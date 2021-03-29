package com.mackhartley.roundedprogressbar

import com.mackhartley.roundedprogressbar.utils.calculateAppropriateCornerRadius
import org.junit.Test

import org.junit.Assert.*

/**
 * Tests the functionality of helper functions for the RoundedProgressBar
 * @see MiscUtils.kt
 */
class RoundedProgressBarHelpersTest {
    @Test
    fun `min allowed corner radius is calculated correctly`() {
        val viewHeight = 20

        val requested1 = 10f // Valid radius
        val requested2 = -5f // Invalid radius: Negative number
        val requested3 = 100f // Invalid radius: Greater than 1/2 view height
        val requested4 = 100f // Set isRadiusRestricted = false for this test

        val expected1 = 10f
        val expected2 = 0f
        val expected3 = (viewHeight / 2f)
        val expected4 = 100f

        assertEquals(expected1, calculateAppropriateCornerRadius(requested1, viewHeight, true))
        assertEquals(expected2, calculateAppropriateCornerRadius(requested2, viewHeight, true))
        assertEquals(expected3, calculateAppropriateCornerRadius(requested3, viewHeight, true))
        assertEquals(expected4, calculateAppropriateCornerRadius(requested4, viewHeight, false))
    }
}