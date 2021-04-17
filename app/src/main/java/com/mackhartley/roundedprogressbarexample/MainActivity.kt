package com.mackhartley.roundedprogressbarexample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.mackhartley.roundedprogressbar.CornerRadius
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

/**
 * Disclaimer: This app was quickly built and is intended to demonstrate the functionality of
 * the RoundedProgressBar library. It may not follow best design practices in all areas. Please do
 * not use it as a judge of good design/architecture.
 */
class MainActivity : AppCompatActivity(), ColorPickerDialogListener {

    private val viewModel by lazy { ViewModelProvider(this).get(MainActivityViewModel::class.java) }

    private lateinit var allProgressBars: List<RoundedProgressBar>

    private companion object {
        private const val ID_PROG_COLOR = 1
        private const val ID_PROG_TEXT_COLOR = 2
        private const val ID_BACKGROUND_COLOR = 3
        private const val ID_BACKGROUND_TEXT_COLOR = 4
        private const val ID_ACTIVITY_BG_COLOR = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_decrease.setOnClickListener { decreaseProgress() }
        button_increase.setOnClickListener { increaseProgress() }
        button_change_amount.setOnClickListener {
            viewModel.nextAmount()
            updateAmountButtonLabel()
        }

        // The state for these isn't held by RPB as they aren't RPB specific
        updateAmountButtonLabel()
        custom_bar_layout.setBackgroundColor(Color.parseColor(viewModel.behindProgBarColor))
        setNewProgressBarHeight(viewModel.progressBarHeight)

        allProgressBars = listOf(
            custom_bar,
            simple_bar_1,
            simple_bar_2,
            simple_bar_3,
            simple_bar_4,
            advanced_bar_1,
            advanced_bar_2,
            advanced_bar_3_top,
            advanced_bar_3_mid,
            advanced_bar_3_bot,
            advanced_bar_4
        )
        setProgressBarAttributesProgrammatically(simple_bar_1)

        populateSettings()
        initSettingsListeners()
    }

    private fun updateAmountButtonLabel() {
        button_change_amount.text = getNewAmountLabel(viewModel.getCurAmount())
    }

    private fun getNewAmountLabel(intVal: Int): String = "+$intVal"

    private fun populateSettings() {
        behind_prog_bar_button.text = viewModel.behindProgBarColor
        prog_bar_height_field.setProgress(viewModel.progressBarHeight)

        prog_color.text = viewModel.progressColor
        prog_text_color.text = viewModel.progressTextColor
        background_color.text = viewModel.backgroundColor
        background_text_color.text = viewModel.backgroundTextColor

        tl_radius_field.setProgress(viewModel.tlRadius)
        tr_radius_field.setProgress(viewModel.trRadius)
        br_radius_field.setProgress(viewModel.brRadius)
        bl_radius_field.setProgress(viewModel.blRadius)

        text_size_field.setProgress(viewModel.textSize)
        text_padding_field.setProgress(viewModel.textPadding)
        animation_length_field.setProgress(viewModel.animLength)

        show_text_switch.isChecked = viewModel.showProgText
        restrict_radius_switch.isChecked = viewModel.restrictRadius
    }

    private fun initSettingsListeners() {
        // Not RPB specific
        behind_prog_bar_button.setOnClickListener {
            ColorPickerDialog.newBuilder()
                .setColor(Color.parseColor(viewModel.behindProgBarColor))
                .setDialogId(ID_ACTIVITY_BG_COLOR).show(this)
        }
        prog_bar_height_field.doOnProgressChanged { _, progress, _ ->
            viewModel.progressBarHeight = progress
            setNewProgressBarHeight(progress)
        }

        // Colors
        prog_color.setOnClickListener {
            ColorPickerDialog.newBuilder()
                .setColor(Color.parseColor(viewModel.progressColor))
                .setDialogId(ID_PROG_COLOR).show(this)
        }
        prog_text_color.setOnClickListener{
            ColorPickerDialog.newBuilder()
                .setColor(Color.parseColor(viewModel.progressTextColor))
                .setDialogId(ID_PROG_TEXT_COLOR).show(this)
        }
        background_color.setOnClickListener{
            ColorPickerDialog.newBuilder()
                .setColor(Color.parseColor(viewModel.backgroundColor))
                .setDialogId(ID_BACKGROUND_COLOR).show(this)
        }
        background_text_color.setOnClickListener {
            ColorPickerDialog.newBuilder()
                .setColor(Color.parseColor(viewModel.backgroundTextColor))
                .setDialogId(ID_BACKGROUND_TEXT_COLOR).show(this)
        }

        // Radius
        tl_radius_field.doOnProgressChanged{ _, progress, _ ->
            viewModel.tlRadius = progress
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.TOP_LEFT)
        }
        tr_radius_field.doOnProgressChanged { _, progress, _ ->
            viewModel.trRadius = progress
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.TOP_RIGHT)
        }
        br_radius_field.doOnProgressChanged { _, progress, _ ->
            viewModel.brRadius = progress
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.BOTTOM_RIGHT)
        }
        bl_radius_field.doOnProgressChanged { _, progress, _ ->
            viewModel.blRadius = progress
            val radius = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setCornerRadius(radius, CornerRadius.BOTTOM_LEFT)
        }

        // Text and Anim
        text_size_field.doOnProgressChanged { _, progress, _ ->
            viewModel.textSize = progress
            val textSize = convertSpToPix(progress.toFloat(), resources)
            custom_bar.setTextSize(textSize)
        }
        text_padding_field.doOnProgressChanged { _, progress, _ ->
            viewModel.textPadding = progress
            val paddingSize = convertDpToPix(progress.toFloat(), resources)
            custom_bar.setTextPadding(paddingSize)
        }
        animation_length_field.doOnProgressChanged { _, progress, _ ->
            viewModel.animLength = progress
            custom_bar.setAnimationLength(progress.toLong())
        }

        // Show Text and Restrict Radius
        show_text_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showProgText = isChecked
            custom_bar.showProgressText(isChecked)
        }
        restrict_radius_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.restrictRadius = isChecked
            custom_bar.setRadiusRestricted(isChecked)
        }
    }

    private fun setNewProgressBarHeight(heightDp: Int) {
        val heightInPix = convertDpToPix(heightDp.toFloat(), resources)
        val newLayoutParams = custom_bar.layoutParams
        newLayoutParams.height = heightInPix.roundToInt()
        custom_bar.layoutParams = newLayoutParams
    }

    /**
     * Example of how to set progress bar attributes programmatically
     */
    private fun setProgressBarAttributesProgrammatically(roundedProgressBar: RoundedProgressBar) {
        roundedProgressBar.setProgressDrawableColor(ContextCompat.getColor(this, R.color.progress_color_s1))
        roundedProgressBar.setBackgroundDrawableColor(ContextCompat.getColor(this, R.color.progress_background_color_s1))
        roundedProgressBar.setTextSize(resources.getDimension(R.dimen.small_text_size))
        roundedProgressBar.setProgressTextColor(ContextCompat.getColor(this, R.color.text_color_s1))
        roundedProgressBar.setBackgroundTextColor(ContextCompat.getColor(this, R.color.bg_text_color_s1))
        roundedProgressBar.showProgressText(true)
    }

    private fun increaseProgress() {
        allProgressBars.forEach { changeProgress(it) }
    }

    private fun decreaseProgress() {
        allProgressBars.forEach { changeProgress(it, false) }
    }

    private fun changeProgress(roundedProgressBar: RoundedProgressBar, isAddition: Boolean = true) {
        val curValue = roundedProgressBar.getProgressPercentage()
        var adjustment = viewModel.getCurAmount()
        if (!isAddition) adjustment *= -1
        roundedProgressBar.setProgressPercentage(curValue + adjustment)
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        when (dialogId) {
            ID_PROG_COLOR -> {
                val colorStr = colorIntToHexString(color)
                prog_color.text = colorStr
                viewModel.progressColor = colorStr
                custom_bar.setProgressDrawableColor(color)
            }
            ID_PROG_TEXT_COLOR -> {
                val colorStr = colorIntToHexString(color)
                prog_text_color.text = colorStr
                viewModel.progressTextColor = colorStr
                custom_bar.setProgressTextColor(color)
            }
            ID_BACKGROUND_COLOR -> {
                val colorStr = colorIntToHexString(color)
                background_color.text = colorStr
                viewModel.backgroundColor = colorStr
                custom_bar.setBackgroundDrawableColor(color)
            }
            ID_BACKGROUND_TEXT_COLOR -> {
                val colorStr = colorIntToHexString(color)
                background_text_color.text = colorStr
                viewModel.backgroundTextColor = colorStr
                custom_bar.setBackgroundTextColor(color)
            }
            ID_ACTIVITY_BG_COLOR -> {
                val colorStr = colorIntToHexString(color)
                behind_prog_bar_button.text = colorStr
                custom_bar_layout.setBackgroundColor(color)
                viewModel.behindProgBarColor = colorStr
            }
        }
    }

    override fun onDialogDismissed(dialogId: Int) {}
}