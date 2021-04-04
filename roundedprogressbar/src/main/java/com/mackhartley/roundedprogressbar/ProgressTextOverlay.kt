package com.mackhartley.roundedprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mackhartley.roundedprogressbar.utils.getPercentageString
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
        const val DEFAULT_TRUE_0 = false
        const val DEFAULT_TRUE_100 = false
    }

    // Default values (Progress text related)
    private val defaultTextSize = context.resources.getDimension(R.dimen.rpb_default_text_size)
    private val defaultTextColor = R.color.rpb_default_text_color
    private val defaultBgTextColor = R.color.rpb_default_text_color
    private val defaultShowProgressText = DEFAULT_SHOW_TEXT
    private val defaultOnlyShowTrue0 = DEFAULT_TRUE_0
    private val defaultOnlyShowTrue100 = DEFAULT_TRUE_100

    // ProgressTextOverlay state
    private var progressValue: Float = START_PROGRESS_VALUE
    private var textSize: Float = defaultTextSize
    private var showProgressText: Boolean = defaultShowProgressText
    private var textContainerHeight: Float = 0f
    private var textContainerWidth: Float = 0f
    private var textSidePadding: Float = context.resources.getDimension(R.dimen.rpb_default_text_padding)
    private var onlyShowTrue0: Boolean = defaultOnlyShowTrue0
    private var onlyShowTrue100: Boolean = defaultOnlyShowTrue100

    // Misc member vars
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
            val requiredTextContainerWidth = textContainerWidth + (2 * textSidePadding)
            val xPosition: Float
            if (requiredTextContainerWidth < progressBarWidth) { // should use inside position
                // Inside position = (Position to draw) - (Width of text) - (Padding of text)
                xPosition = (width * progressValue) - textContainerWidth - textSidePadding
                canvas?.drawText(getPercentageString(progressValue, onlyShowTrue0, onlyShowTrue100), xPosition, yPosition, progressTextOverlayPaint)
            } else { // should use outside position
                // Outside position = (Position to draw) + (Padding of text)
                xPosition = (width * progressValue) + textSidePadding
                canvas?.drawText(getPercentageString(progressValue, onlyShowTrue0, onlyShowTrue100), xPosition, yPosition, backgroundTextOverlayPaint)
            }
        }
    }

    /**
     * Not used often, mainly when first initializing the view or changing text size
     */
    private fun reCalculateTextHeight() {
        progressTextOverlayPaint.textSize = textSize
        backgroundTextOverlayPaint.textSize = textSize
        val newProgressString = getPercentageString(progressValue, onlyShowTrue0, onlyShowTrue100)

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

        val newProgressString = getPercentageString(progressValue, onlyShowTrue0, onlyShowTrue100)
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
    fun setProgressTextColor(@ColorInt newColor: Int) {
        progressTextOverlayPaint.color = newColor
        invalidate()
    }

    /**
     * This sets the text color for when the Text Overlay is drawn over the background. i.e. the
     * Text Overlay isn't drawn inside the progress bar
     */
    fun setBackgroundTextColor(@ColorInt newColor: Int) {
        backgroundTextOverlayPaint.color = newColor
        invalidate()
    }

    fun showProgressText(newShowProgressText: Boolean) {
        this.showProgressText = newShowProgressText
        invalidate()
    }

    fun setTextPadding(newTextPadding: Float) {
        this.textSidePadding = newTextPadding
        reCalculateTextWidth()
        invalidate()
    }

    fun setOnlyShowTrue0(shouldOnlyShowTrue0: Boolean) {
        this.onlyShowTrue0 = shouldOnlyShowTrue0
        reCalculateTextWidth()
        invalidate()
    }

    fun setOnlyShowTrue100(shouldOnlyShowTrue100: Boolean) {
        this.onlyShowTrue100 = shouldOnlyShowTrue100
        reCalculateTextWidth()
        invalidate()
    }
}