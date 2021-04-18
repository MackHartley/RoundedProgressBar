package com.mackhartley.roundedprogressbar

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertEquals
import org.junit.Before

/**
 * Tests the functionality of the RoundedProgressBar
 * @see RoundedProgressBar
 */
@RunWith(AndroidJUnit4::class)
class RoundedProgressBarTest {

    private lateinit var progressBar: RoundedProgressBar

    @Before
    fun setUpProgressBar() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        progressBar = RoundedProgressBar(appContext)
    }

    @Test
    fun verifyProgressValueBehavior() {
        val expectedStart = 0.0

        val provided1 = 20.0
        val expected1 = 20.0

        val provided2 = 33.333
        val expected2 = 33.333

        val provided3 = 0.0
        val expected3 = 0.0

        val provided4 = 100.0
        val expected4 = 100.0

        val provided5 = -10.0
        val expected5 = 0.0

        val provided6 = 110.0
        val expected6 = 100.0

        val delta = 0.0

        val startingValue = progressBar.getProgressPercentage()
        assertEquals(expectedStart, startingValue, delta)

        // 1) Check round number
        progressBar.setProgressPercentage(provided1, false)
        val progressValue1 = progressBar.getProgressPercentage()
        assertEquals(expected1, progressValue1, delta)

        // 2) Check number with decimal
        progressBar.setProgressPercentage(provided2, false)
        val progressValue2 = progressBar.getProgressPercentage()
        assertEquals(expected2, progressValue2, delta)

        // 3) Check setting value to min works
        progressBar.setProgressPercentage(provided3, false)
        val progressValue3 = progressBar.getProgressPercentage()
        assertEquals(expected3, progressValue3, delta)

        // 4) Check setting value to max works
        progressBar.setProgressPercentage(provided4, false)
        val progressValue4 = progressBar.getProgressPercentage()
        assertEquals(expected4, progressValue4, delta)

        // 5) Check setting invalid low value is handled
        progressBar.setProgressPercentage(provided5, false)
        val progressValue5 = progressBar.getProgressPercentage()
        assertEquals(expected5, progressValue5, delta)

        // 6) Check setting invalid high value is handled
        progressBar.setProgressPercentage(provided6, false)
        val progressValue6 = progressBar.getProgressPercentage()
        assertEquals(expected6, progressValue6, delta)
    }
}
