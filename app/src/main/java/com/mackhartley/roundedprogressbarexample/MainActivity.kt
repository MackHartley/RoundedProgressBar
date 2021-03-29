package com.mackhartley.roundedprogressbarexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var allProgressBars: List<RoundedProgressBar>
    private lateinit var progBarsModifyCorners: List<RoundedProgressBar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allProgressBars = listOf(
            bar_modified_programmatically,
            bar_modified_via_xml,
            extra_bar_1,
            extra_bar_2,
            bar_5,
            multi_bar_1,
            multi_bar_2,
            multi_bar_3
        )
        progBarsModifyCorners = listOf(
            bar_modified_programmatically,
            bar_modified_via_xml,
            extra_bar_1,
            extra_bar_2
        )

        setProgressBarAttributesProgrammatically(bar_modified_programmatically)

        button_increase.setOnClickListener { increaseProgress() }
        button_decrease.setOnClickListener { decreaseProgress() }
        button_corner_radius.setOnClickListener { setRandomCornerRadius() }
    }

    private fun setProgressBarAttributesProgrammatically(roundedProgressBar: RoundedProgressBar) {
        roundedProgressBar.setProgressColor(R.color.progress_color_1)
        roundedProgressBar.setProgressBgColor(R.color.progress_background_color_1)
        roundedProgressBar.setTextSize(resources.getDimension(R.dimen.small_text_size))
        roundedProgressBar.setTextColor(R.color.text_color_1)
        roundedProgressBar.setBgTextColor(R.color.bg_text_color_1)
        roundedProgressBar.showProgressText(true)
        roundedProgressBar.setAnimationLength(900)
    }

    private fun increaseProgress() {
        allProgressBars.forEach { changeProgress(it) }
    }

    private fun decreaseProgress() {
        allProgressBars.forEach { changeProgress(it, false) }
    }

    private fun setRandomCornerRadius() {
        val newDimen = when (Random.nextInt(0, 7)) {
            0 -> resources.getDimension(R.dimen.random_corner_radius_0)
            1 -> resources.getDimension(R.dimen.random_corner_radius_4)
            2 -> resources.getDimension(R.dimen.random_corner_radius_8)
            3 -> resources.getDimension(R.dimen.random_corner_radius_12)
            4 -> resources.getDimension(R.dimen.random_corner_radius_16)
            5 -> resources.getDimension(R.dimen.random_corner_radius_32)
            else -> resources.getDimension(R.dimen.random_corner_radius_40)
        }
        progBarsModifyCorners.forEach { it.setCornerRadius(newDimen) }
    }

    private fun getIncrement(): Double {
        return 25.0
    }

    private fun changeProgress(roundedProgressBar: RoundedProgressBar, isAddition: Boolean = true) {
        val curValue = roundedProgressBar.getProgressPercentage()
        var adjustment = getIncrement()
        if (!isAddition) adjustment *= -1
        roundedProgressBar.setProgressPercentage(curValue + adjustment)
    }
}