package com.mackhartley.roundedprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.max

internal class ProgressTextOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        // Used to calculate the min width of text view. This is done for UX reasons, to
        //   minimize how often the display number swaps between the inside and external position.
        //   Without this minimum, 8% could be outside the bar, 9% on inside (since the bar grew)
        //   and 10% on outside. All that swapping looks less smooth to the user
        const val MIN_WIDTH_STRING = "10%"
        const val START_PROGRESS_VALUE = 0f
        const val DEFAULT_SHOW_TEXT = true
    }

    // Default values (Progress text related)
    private val defaultTextSize = context.resources.getDimension(R.dimen.rpb_default_text_size)
    private val defaultTextColor = R.color.rpb_default_text_color
    private val defaultBgTextColor = R.color.rpb_default_text_color
    private val defaultShowProgressText = DEFAULT_SHOW_TEXT

    // ProgressTextOverlay state
    private var progressValue: Float = START_PROGRESS_VALUE
    private var textSize: Float = defaultTextSize
    private var showProgressText: Boolean = defaultShowProgressText
    private var textContainerHeight: Float = 0f
    private var textContainerWidth: Float = 0f

    // Misc member vars
    private val textPadding = context.resources.getDimension(R.dimen.rpb_text_padding)
    private val progressTextOverlayPaint: Paint
    private val backgroundTextOverlayPaint: Paint
    private val boundingRect = Rect() // Used for calculating measurements of progress text

    init {
        // init text color paint
        val newPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        newPaint.color = ContextCompat.getColor(context, defaultTextColor)
        progressTextOverlayPaint = newPaint

        // init background text color paint
        val newAltPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        newAltPaint.color = ContextCompat.getColor(context, defaultBgTextColor)
        backgroundTextOverlayPaint = newAltPaint

        reCalculateTextHeight() // Sets the initial text size (MUST BE LAST STEP IN INIT)
    }

    override fun onDraw(canvas: Canvas?) {
        if (showProgressText) {
            super.onDraw(canvas)

            val yPosition = (height) / 2 + (textContainerHeight / 2)

            val progressBarWidth = width * progressValue
            val requiredTextContainerWidth = textContainerWidth + (2 * textPadding)
            val xPosition: Float
            if (requiredTextContainerWidth < progressBarWidth) { // should use inside position
                // Inside position = (Position to draw) - (Width of text) - (Padding of text)
                xPosition = (width * progressValue) - textContainerWidth - textPadding
                canvas?.drawText(getPercentageString(progressValue), xPosition, yPosition, progressTextOverlayPaint)
            } else { // should use outside position
                // Outside position = (Position to draw) + (Padding of text)
                xPosition = (width * progressValue) + textPadding
                canvas?.drawText(getPercentageString(progressValue), xPosition, yPosition, backgroundTextOverlayPaint)
            }
        }
    }

    private fun getPercentageString(float: Float): String {
        val percentage = float * 100
        return "${percentage.toInt()}%" // Uses rounding down. 100% won't be shown in the event progress = 99.5%. 100% only shown upon full completion
    }

    /**
     * Not used often, mainly when first initializing the view or changing text size
     */
    private fun reCalculateTextHeight() {
        progressTextOverlayPaint.textSize = textSize
        backgroundTextOverlayPaint.textSize = textSize
        val newProgressString = getPercentageString(progressValue)

        progressTextOverlayPaint.getTextBounds(newProgressString, 0, newProgressString.length, boundingRect)
        val measuredSize = boundingRect.height().toFloat()

        textContainerHeight = measuredSize
    }

    /**
     * Called often, when initializing the view, changing text size, or updating the displayed
     * progress number
     */
    private fun reCalculateTextWidth() {
        progressTextOverlayPaint.textSize = textSize
        backgroundTextOverlayPaint.textSize = textSize

        val newProgressString = getPercentageString(progressValue)
        progressTextOverlayPaint.getTextBounds(newProgressString, 0, newProgressString.length, boundingRect)
        val measuredSize = boundingRect.width().toFloat()

        progressTextOverlayPaint.getTextBounds(MIN_WIDTH_STRING, 0, MIN_WIDTH_STRING.length, boundingRect)
        val minSizeAllowed = boundingRect.width().toFloat()

        textContainerWidth = max(measuredSize, minSizeAllowed)
    }

    // ################################## //
    // ######### PUBLIC METHODS ######### //
    // ################################## //
    fun setProgress(newProgress: Float) {
        this.progressValue = newProgress
        reCalculateTextWidth() // Need to call this because the text overlay string changes between animations. e.g. 4% -> 12% is a different width
        invalidate()
    }

    fun setTextSize(newTextSize: Float) {
        this.textSize = newTextSize
        reCalculateTextHeight()
        reCalculateTextWidth()
        invalidate()
    }

    /**
     * This sets the text color for when the Text Overlay is drawn over the progress bar
     */
    fun setTextColor(newColorRes: Int) {
        val newColor = ContextCompat.getColor(context, newColorRes)
        progressTextOverlayPaint.color = newColor
        invalidate()
    }

    /**
     * This sets the text color for when the Text Overlay is drawn over the background. i.e. the
     * Text Overlay isn't drawn inside the progress bar
     */
    fun setBgTextColor(newColorRes: Int) {
        val newColor = ContextCompat.getColor(context, newColorRes)
        backgroundTextOverlayPaint.color = newColor
        invalidate()
    }

    fun showProgressText(newShowProgressText: Boolean) {
        this.showProgressText = newShowProgressText
        invalidate()
    }
}