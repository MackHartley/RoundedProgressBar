package com.mackhartley.roundedprogressbarexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var roundedProgressBar1: RoundedProgressBar
    private lateinit var roundedProgressBar2: RoundedProgressBar
    private lateinit var roundedProgressBar3: RoundedProgressBar
    private lateinit var roundedProgressBar4: RoundedProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        collectProgressBars()
        setProgressBarAttributesProgrammatically(roundedProgressBar1)

        button_increase.setOnClickListener { increaseProgress() }
        button_decrease.setOnClickListener { decreaseProgress() }
        button_corner_radius.setOnClickListener { setRandomCornerRadius() }
    }

    private fun collectProgressBars() {
        roundedProgressBar1 = bar_modified_programmatically
        roundedProgressBar2 = bar_modified_via_xml
        roundedProgressBar3 = extra_bar_1
        roundedProgressBar4 = extra_bar_2
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
        changeProgress(roundedProgressBar1)
        changeProgress(roundedProgressBar2)
        changeProgress(roundedProgressBar3)
        changeProgress(roundedProgressBar4)
    }

    private fun decreaseProgress() {
        changeProgress(roundedProgressBar1, false)
        changeProgress(roundedProgressBar2, false)
        changeProgress(roundedProgressBar3, false)
        changeProgress(roundedProgressBar4, false)
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
        roundedProgressBar1.setCornerRadius(newDimen)
        roundedProgressBar2.setCornerRadius(newDimen)
        roundedProgressBar3.setCornerRadius(newDimen)
        roundedProgressBar4.setCornerRadius(newDimen)
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