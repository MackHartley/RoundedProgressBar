package com.mackhartley.roundedprogressbar

import com.mackhartley.roundedprogressbar.utils.calculateAppropriateCornerRadius
import com.mackhartley.roundedprogressbar.utils.getPercentageString
import org.junit.Test

import org.junit.Assert.*

/**
 * Tests the functionality of helper functions for the [RoundedProgressBar] and [ProgressTextOverlay]
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

    @Test
    fun `progress text to show is formatted correctly`() {
        val provided1 = 0f
        val expected1 = "0%"
        assertEquals(expected1, getPercentageString(provided1, true, true))
        assertEquals(expected1, getPercentageString(provided1, false, true))
        assertEquals(expected1, getPercentageString(provided1, true, false))
        assertEquals(expected1, getPercentageString(provided1, false, false))

        val provided2 = 0.004f
        val expected2a = "1%" // Don't show a 0 if the value isn't actually 0 and onlyShowTrue0 == true
        val expected2b = "0%"
        assertEquals(expected2a, getPercentageString(provided2, true, true))
        assertEquals(expected2a, getPercentageString(provided2, true, false))
        assertEquals(expected2b, getPercentageString(provided2, false, true))
        assertEquals(expected2b, getPercentageString(provided2, false, false))

        val provided3 = 0.005f
        val expected3 = "1%"
        assertEquals(expected3, getPercentageString(provided3, true, true))
        assertEquals(expected3, getPercentageString(provided3, true, false))
        assertEquals(expected3, getPercentageString(provided3, false, true))
        assertEquals(expected3, getPercentageString(provided3, false, false))

        val provided4 = 0.11f
        val expected4 = "11%"
        assertEquals(expected4, getPercentageString(provided4, true, true))
        assertEquals(expected4, getPercentageString(provided4, true, false))
        assertEquals(expected4, getPercentageString(provided4, false, true))
        assertEquals(expected4, getPercentageString(provided4, false, false))

        val provided5 = 0.99f
        val expected5 = "99%"
        assertEquals(expected5, getPercentageString(provided5, true, true))
        assertEquals(expected5, getPercentageString(provided5, false, true))
        assertEquals(expected5, getPercentageString(provided5, true, false))
        assertEquals(expected5, getPercentageString(provided5, false, false))

        val provided6 = 0.994f
        val expected6 = "99%"
        assertEquals(expected6, getPercentageString(provided6, true, true))
        assertEquals(expected6, getPercentageString(provided6, false, true))
        assertEquals(expected6, getPercentageString(provided6, true, false))
        assertEquals(expected6, getPercentageString(provided6, false, false))

        val provided7 = 0.995f
        val expected7a = "99%" // Don't show 100 if the true value isn't actually 100 yet
        val expected7b = "100%"
        assertEquals(expected7a, getPercentageString(provided7, true, true))
        assertEquals(expected7a, getPercentageString(provided7, false, true))
        assertEquals(expected7b, getPercentageString(provided7, true, false))
        assertEquals(expected7b, getPercentageString(provided7, false, false))

        val provided8 = 1.00f
        val expected8 = "100%"
        assertEquals(expected8, getPercentageString(provided8, true, true))
        assertEquals(expected8, getPercentageString(provided8, false, true))
        assertEquals(expected8, getPercentageString(provided8, true, false))
        assertEquals(expected8, getPercentageString(provided8, false, false))
    }
}