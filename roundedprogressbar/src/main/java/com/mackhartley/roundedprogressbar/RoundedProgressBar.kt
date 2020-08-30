package com.mackhartley.roundedprogressbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.mackhartley.roundedprogressbar.ProgressTextOverlay.Companion.DEFAULT_SHOW_TEXT
import com.mackhartley.roundedprogressbar.ext.setColorFilterCompat
import kotlinx.android.synthetic.main.layout_rounded_progress_bar.view.*

class RoundedProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_PROGRESS = 100.0
        private const val MIN_PROGRESS = 0.0
        private const val PROGRESS_BAR_MAX = 100
        private const val PROGRESS_SCALAR =
            10 // This is done to make the animation more fine grain and thus smoother
        private const val INITIAL_PROGRESS_VALUE = 0
    }

    // Default values (ProgressBar related)
    private val defaultProgressValue = INITIAL_PROGRESS_VALUE
    private val defaultProgressColor = R.color.rpb_default_progress_color
    private val defaultProgressBgColor = R.color.rpb_default_progress_bg_color
    private val defaultAnimationLength = context.resources.getInteger(R.integer.rpb_default_animation_duration)

    // Default values (ProgressTextOverlay related)
    private val defaultTextSize: Float = context.resources.getDimension(R.dimen.rpb_default_text_size)
    private val defaultTextColorRes: Int = R.color.rpb_default_text_color
    private val defaultBgTextColorRes: Int = R.color.rpb_default_text_color
    private val defaultShowProgressText: Boolean = DEFAULT_SHOW_TEXT

    // ProgressBar state
    private var curProgress: Double = INITIAL_PROGRESS_VALUE.toDouble()
    private var prevTextPositionRatio: Float = INITIAL_PROGRESS_VALUE.toFloat() // Used to keep track of the ProgressTextOverlay position during an animation. Allows for smooth transitions between a current and interrupting animation
    private var progressColorRes: Int = defaultProgressColor
    private var progressBgColorRes: Int = defaultProgressBgColor
    private var animationLength: Long = defaultAnimationLength.toLong()

    // ProgressTextOverlay state
    private var textSize: Float = defaultTextSize
    private var textColorRes: Int = defaultTextColorRes
    private var bgTextColorRes: Int = defaultBgTextColorRes
    private var showProgressText: Boolean = defaultShowProgressText

    // Progress bar objects
    private val progressBar: ProgressBar
    private val progressDrawable: Drawable
    private val progressBgDrawable: Drawable
    private val progressTextOverlay: ProgressTextOverlay

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_rounded_progress_bar, this, false)
        progressBar = view.rounded_progress_bar
        val progressBarLayers = progressBar.progressDrawable as LayerDrawable
        progressDrawable = progressBarLayers.getDrawable(1)
        progressBgDrawable = progressBarLayers.getDrawable(0)
        progressTextOverlay = view.progress_text_overlay
        progressBar.max = PROGRESS_BAR_MAX * PROGRESS_SCALAR // This is done so animations look smoother
        clipProgressBarCorners() // This method ensures that the progress background doesn't look weird at lower values
        initAttributes(attrs)
        addView(view)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        if (attrs == null) return
        val rpbAttributes = context.obtainStyledAttributes(attrs, R.styleable.RoundedProgressBar)

        // Set progress from xml attributes (If exists and isn't the default value)
        val newProgressValue = rpbAttributes.getInteger(R.styleable.RoundedProgressBar_rpbProgress, defaultProgressValue)
        if (newProgressValue != defaultProgressValue) setProgressPercentage(newProgressValue.toDouble())

        // Set progress bar color via xml (If exists and isn't the default value)
        val newProgressBarColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbProgressColor, defaultProgressColor)
        if (newProgressBarColor != defaultProgressColor) setProgressColor(newProgressBarColor)

        // Set progress bar background via xml (If exists and isn't the default value)
        val newProgressBgColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbProgressBgColor, defaultProgressBgColor)
        if (newProgressBgColor != defaultProgressBgColor) setProgressBgColor(newProgressBgColor)

        // Set text size from xml attributes (If exists and isn't the default value)
        val newTextSize = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbTextSize, defaultTextSize)
        if (newTextSize != defaultTextSize) setTextSize(newTextSize)

        // Set progress bar text color via xml (If exists and isn't the default value)
        val newTextColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbTextColor, defaultTextColorRes)
        if (newTextColor != defaultTextColorRes) setTextColor(newTextColor)

        // Set background text color via xml (If exists and isn't the default value)
        val newBgTextColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbBgTextColor, defaultBgTextColorRes)
        if (newBgTextColor != defaultBgTextColorRes) setBgTextColor(newBgTextColor)

        // Show or hide progress text via xml (If exists and isn't the default value)
        val newShowProgressText = rpbAttributes.getBoolean(R.styleable.RoundedProgressBar_rpbShowProgressText, defaultShowProgressText)
        if (newShowProgressText != defaultShowProgressText) progressTextOverlay.showProgressText(newShowProgressText)

        // Set animation length via xml (If exists and isn't the default value)
        val newAnimationLength = rpbAttributes.getInteger(R.styleable.RoundedProgressBar_rpbAnimationLength, defaultAnimationLength)
        if (newAnimationLength != defaultAnimationLength) setAnimationLength(newAnimationLength.toLong())

        rpbAttributes.recycle()
    }

    private fun clipProgressBarCorners() {
        progressDrawable.apply {
            val provider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, view.height / 2f)
                }
            }
            outlineProvider = provider
            clipToOutline = true
        }
    }

    /**
     * @return a double between @param[MIN_PROGRESS] and @param[MAX_PROGRESS] inclusive
     */
    private fun getNormalizedValue(progressPercentage: Double): Double {
        return when {
            progressPercentage < MIN_PROGRESS -> MIN_PROGRESS
            progressPercentage > MAX_PROGRESS -> MAX_PROGRESS
            else -> progressPercentage
        }
    }

    /**
     * @return the given progress value, but scaled to match the scale of the progress bar
     * e.g. 48.0 -> 480
     */
    private fun getScaledProgressValue(preScaledValue: Double): Int {
        return (preScaledValue * PROGRESS_SCALAR).toInt()
    }

    /**
     * @return the ratio of how long the progress bar is compared to its container. This ratio
     * is then used to calculate the position of the @see[ProgressTextOverlay]
     */
    private fun getTextPositionRatio(progressPercentage: Double): Float {
        return (progressPercentage / PROGRESS_BAR_MAX).toFloat()
    }

    // ################################## //
    // ## SAVE STATE BOILERPLATE CODE ### //
    // ################################## //

    public override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.savedCurProgress = curProgress
        savedState.savedPrevTextPositionRatio = prevTextPositionRatio
        savedState.savedProgressColorRes = progressColorRes
        savedState.savedProgressBgColorRes = progressBgColorRes
        savedState.savedAnimationLength = animationLength

        savedState.savedTextSize = textSize
        savedState.savedTextColorRes = textColorRes
        savedState.savedBgTextColorRes = bgTextColorRes
        savedState.savedShowProgressText = showProgressText
        return savedState
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            // RoundedProgressBar related
            curProgress = state.savedCurProgress
            prevTextPositionRatio = state.savedPrevTextPositionRatio
            progressColorRes = state.savedProgressColorRes
            progressBgColorRes = state.savedProgressBgColorRes
            animationLength = state.savedAnimationLength
            setProgressPercentage(curProgress, false)
            setProgressColor(progressColorRes)
            setProgressBgColor(progressBgColorRes)
            // ProgressTextOverlay related
            textSize = state.savedTextSize
            textColorRes = state.savedTextColorRes
            bgTextColorRes = state.savedBgTextColorRes
            showProgressText = state.savedShowProgressText
            setTextSize(textSize)
            setTextColor(textColorRes)
            setBgTextColor(bgTextColorRes)
            showProgressText(showProgressText)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    // Props to the person who wrote this, saved me from going crazy:
    // https://www.netguru.com/codestories/how-to-correctly-save-the-state-of-a-custom-view-in-android
    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    internal class SavedState : View.BaseSavedState {
        // RoundedProgressBar related
        var savedCurProgress: Double = 0.0
        var savedPrevTextPositionRatio: Float = 0f
        var savedProgressColorRes: Int = 0
        var savedProgressBgColorRes: Int = 0
        var savedAnimationLength: Long = 0L

        // ProgressTextOverlay related
        var savedTextColorRes: Int = 0
        var savedTextSize: Float = 0f
        var savedBgTextColorRes: Int = 0
        var savedShowProgressText: Boolean = true

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            savedCurProgress = source.readDouble()
            savedPrevTextPositionRatio = source.readFloat()
            savedProgressColorRes = source.readInt()
            savedProgressBgColorRes = source.readInt()
            savedAnimationLength = source.readLong()

            savedTextSize = source.readFloat()
            savedTextColorRes = source.readInt()
            savedBgTextColorRes = source.readInt()
            savedShowProgressText = source.readByte() != 0.toByte()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeDouble(savedCurProgress)
            out.writeFloat(savedPrevTextPositionRatio)
            out.writeInt(savedProgressColorRes)
            out.writeInt(savedProgressBgColorRes)
            out.writeLong(savedAnimationLength)

            out.writeFloat(savedTextSize)
            out.writeInt(savedTextColorRes)
            out.writeInt(savedBgTextColorRes)
            out.writeByte(if (savedShowProgressText) 1.toByte() else 0.toByte())
        }

        companion object {
            @Suppress("UNUSED")
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    // ################################## //
    // ######### PUBLIC METHODS ######### //
    // ################################## //
    /**
     * @param[progressPercentage] is a value between 0 and 100 inclusive representing the percent
     * completion of the progress bar. Any values outside this range will be normalized to be inside
     * the range
     *
     * @param[shouldAnimate] if set to false, the progress bar wont animate for this specific call
     */
    fun setProgressPercentage(progressPercentage: Double, shouldAnimate: Boolean = true) {
        val normalizedProgress = getNormalizedValue(progressPercentage)

        // Calculate new progress value for progress bar and the text overlay
        val scaledProgressValue = getScaledProgressValue(normalizedProgress)
        val textPositionRatio = getTextPositionRatio(normalizedProgress)

        if (shouldAnimate) {
            // Update the progress values and animate the changes
            val barAnim = ObjectAnimator
                .ofInt(progressBar, "progress", scaledProgressValue)
                .setDuration(animationLength)
            val textAnim = ObjectAnimator
                .ofFloat(progressTextOverlay, "progress", prevTextPositionRatio, textPositionRatio)
                .setDuration(animationLength)
            textAnim.addUpdateListener {
                prevTextPositionRatio = (it.animatedValue as? Float) ?: 0f
            }
            val animSet = AnimatorSet().apply {
                play(barAnim).with(textAnim)
            }
            animSet.start()
        } else {
            progressBar.progress = scaledProgressValue
            progressTextOverlay.setProgress(textPositionRatio)
        }

        prevTextPositionRatio = textPositionRatio
        curProgress = normalizedProgress
    }

    fun getProgressPercentage(): Double {
        return curProgress
    }

    fun setProgressColor(colorRes: Int) {
        progressColorRes = colorRes
        progressDrawable.setColorFilterCompat(context, colorRes)
    }

    fun setProgressBgColor(colorRes: Int) {
        progressBgColorRes = colorRes
        progressBgDrawable.setColorFilterCompat(context, colorRes)
    }

    /**
     * Sets the text color of text which appears on top of the progress bar (The completed portion)
     */
    fun setTextColor(colorRes: Int) {
        textColorRes = colorRes
        progressTextOverlay.setTextColor(colorRes)
    }

    /**
     * Sets the text color of text which appears on top of the background
     */
    fun setBgTextColor(colorRes: Int) {
        bgTextColorRes = colorRes
        progressTextOverlay.setBgTextColor(colorRes)
    }

    fun setTextSize(sizeInPixels: Float) {
        textSize = sizeInPixels
        progressTextOverlay.setTextSize(sizeInPixels)
    }

    /**
     * Can be used to hide or show the @see[ProgressTextOverlay] view
     */
    fun showProgressText(shouldShowProgressText: Boolean) {
        progressTextOverlay.showProgressText(shouldShowProgressText)
    }

    fun setAnimationLength(newAnimationLength: Long) {
        animationLength = newAnimationLength
    }
}