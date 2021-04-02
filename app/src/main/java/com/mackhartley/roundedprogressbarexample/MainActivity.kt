package com.mackhartley.roundedprogressbarexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.mackhartley.roundedprogressbar.CornerRadius
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ColorPickerDialogListener {

    private lateinit var allProgressBars: List<RoundedProgressBar>
    private lateinit var progBarsModifyCorners: List<RoundedProgressBar>

    private companion object {
        private const val ID_PROG_COLOR = 1
        private const val ID_PROG_TEXT_COLOR = 2
        private const val ID_BACKGROUND_COLOR = 3
        private const val ID_BACKGROUND_TEXT_COLOR = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allProgressBars = listOf(
            custom_bar,
            simple_bar_1,
            simple_bar_2,
            simple_bar_3,
            advanced_bar_1,
            advanced_bar_2,
            advanced_bar_3,
            advanced_bar_4_top,
            advanced_bar_4_mid,
            advanced_bar_4_bot,
            advanced_bar_5
        )
        progBarsModifyCorners = listOf(
            simple_bar_1,
            simple_bar_2,
            simple_bar_3
        )
        setProgressBarAttributesProgrammatically(simple_bar_1)

        button_increase.setOnClickListener { increaseProgress() }
        button_decrease.setOnClickListener { decreaseProgress() }


        // TODO Add ability to change inc/dec amount
        // TOdo initalize correct start vars
        // Todo get viewmodel
        // Todo change simple/advanced headers
        // todo remove toolbar

        prog_color.setOnClickListener {
            ColorPickerDialog.newBuilder().setDialogId(ID_PROG_COLOR).show(this)
        }
        prog_text_color.setOnClickListener {
            ColorPickerDialog.newBuilder().setDialogId(ID_PROG_TEXT_COLOR).show(this)
        }
        background_color.setOnClickListener {
            ColorPickerDialog.newBuilder().setDialogId(ID_BACKGROUND_COLOR).show(this)
        }
        background_text_color.setOnClickListener {
            ColorPickerDialog.newBuilder().setDialogId(ID_BACKGROUND_TEXT_COLOR).show(this)
        }

        tl_radius_field.doOnProgressChanged { _, progress, _ ->
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.TOP_LEFT)
        }
        tr_radius_field.doOnProgressChanged { _, progress, _ ->
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.TOP_RIGHT)
        }
        br_radius_field.doOnProgressChanged { _, progress, _ ->
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.BOTTOM_RIGHT)
        }
        bl_radius_field.doOnProgressChanged { _, progress, _ ->
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.BOTTOM_LEFT)
        }

        text_size_field.doOnProgressChanged { _, progress, _ ->
            val textSize = convertSpToPix(progress.toFloat(), resources)
            custom_bar.setTextSize(textSize)
        }
        text_padding_field.doOnProgressChanged { _, progress, _ ->
            val paddingSize = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setTextPadding(paddingSize)
        }
        animation_length_field.doOnProgressChanged { _, progress, _ ->
            custom_bar.setAnimationLength(progress.toLong())
        }

        show_text_switch.setOnCheckedChangeListener { _, isChecked ->
            custom_bar.showProgressText(isChecked)
        }
        restrict_radius_switch.setOnCheckedChangeListener { _, isChecked ->
            custom_bar.setRadiusRestricted(isChecked)
        }
    }



    /**
     * Example of how to set progress bar attributes programatically
     */
    private fun setProgressBarAttributesProgrammatically(roundedProgressBar: RoundedProgressBar) {
        roundedProgressBar.setProgressColor(ContextCompat.getColor(this, R.color.progress_color_s1))
        roundedProgressBar.setProgressBackgroundColor(ContextCompat.getColor(this, R.color.progress_background_color_s1))
        roundedProgressBar.setTextSize(resources.getDimension(R.dimen.small_text_size))
        roundedProgressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.text_color_s1))
        roundedProgressBar.setBackgroundTextColor(ContextCompat.getColor(this, R.color.bg_text_color_s1))
        roundedProgressBar.showProgressText(true)
        roundedProgressBar.setAnimationLength(900)
    }

    private fun increaseProgress() {
        allProgressBars.forEach { changeProgress(it) }
    }

    private fun decreaseProgress() {
        allProgressBars.forEach { changeProgress(it, false) }
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

    override fun onColorSelected(dialogId: Int, color: Int) {
        when (dialogId) {
            ID_PROG_COLOR -> {
                prog_color.text = colorIntToHexString(color)
                custom_bar.setProgressColor(color)
            }
            ID_PROG_TEXT_COLOR -> {
                prog_text_color.text = colorIntToHexString(color)
                custom_bar.setProgressTextColor(color)
            }
            ID_BACKGROUND_COLOR -> {
                background_color.text = colorIntToHexString(color)
                custom_bar.setProgressBackgroundColor(color)
            }
            else -> {
                background_text_color.text = colorIntToHexString(color)
                custom_bar.setBackgroundTextColor(color)
            }
        }
    }

    override fun onDialogDismissed(dialogId: Int) {}
}