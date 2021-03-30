package com.mackhartley.roundedprogressbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.mackhartley.roundedprogressbar.ProgressTextOverlay.Companion.DEFAULT_SHOW_TEXT
import com.mackhartley.roundedprogressbar.ext.setColorFilterCompat
import com.mackhartley.roundedprogressbar.utils.calculateAppropriateCornerRadius
import kotlinx.android.synthetic.main.layout_rounded_progress_bar.view.*
import kotlin.math.roundToInt

class RoundedProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_PROGRESS = 100.0
        private const val MIN_PROGRESS = 0.0
        private const val PROGRESS_BAR_MAX = 100
        private const val PROGRESS_SCALAR = 10 // This is done to make the progress bar animation more fine grain and thus smoother
        private const val INITIAL_PROGRESS_VALUE = 0
        private const val SCALE_DRAWABLE_MULTIPLIER = 100.0
        private const val PROG_BACKGROUND_LAYER_INDEX = 0
        private const val PROG_DRAWABLE_LAYER_INDEX = 1
        private const val NO_CORNER_RADIUS_ATTR_SET = -1f
    }

    // Default values (ProgressBar related)
    private val defaultProgressValue = INITIAL_PROGRESS_VALUE
    private val defaultProgressColor = R.color.rpb_default_progress_color
    private val defaultProgressBgColor = R.color.rpb_default_progress_bg_color
    private val defaultAnimationLength = context.resources.getInteger(R.integer.rpb_default_animation_duration)
    private val defaultCornerRadius = context.resources.getDimension(R.dimen.rpb_default_corner_radius)
    private val defaultIsRadiusRestricted = true
    // Default values (ProgressTextOverlay related)
    private val defaultTextSize: Float = context.resources.getDimension(R.dimen.rpb_default_text_size)
    private val defaultTextColorRes: Int = R.color.rpb_default_text_color
    private val defaultBgTextColorRes: Int = R.color.rpb_default_text_color
    private val defaultShowProgressText: Boolean = DEFAULT_SHOW_TEXT
    private val defaultTextPadding: Float = context.resources.getDimension(R.dimen.rpb_default_text_padding)

    // Instance state (ProgressBar related)
    private var curProgress: Double = INITIAL_PROGRESS_VALUE.toDouble()
    private var prevTextPositionRatio: Float = INITIAL_PROGRESS_VALUE.toFloat() // Used to keep track of the ProgressTextOverlay position during an animation. Allows for smooth transitions between a current and interrupting animation
    private var progressColorRes: Int = defaultProgressColor
    private var progressBgColorRes: Int = defaultProgressBgColor
    private var animationLength: Long = defaultAnimationLength.toLong()
    private var cornerRadiusTL: Float = defaultCornerRadius // Top Left
    private var cornerRadiusTR: Float = defaultCornerRadius // Top Right
    private var cornerRadiusBR: Float = defaultCornerRadius // Bottom Right
    private var cornerRadiusBL: Float = defaultCornerRadius // Bottom Left
    private var isRadiusRestricted: Boolean = defaultIsRadiusRestricted
    private var lastReportedHeight: Int = 0
    private var lastReportedWidth: Int = 0
    // Instance state (ProgressTextOverlay related)
    private var textSize: Float = defaultTextSize
    private var textColorRes: Int = defaultTextColorRes
    private var bgTextColorRes: Int = defaultBgTextColorRes
    private var showProgressText: Boolean = defaultShowProgressText
    private var textPadding: Float = defaultTextPadding

    // Progress bar objects
    private val progressBar: ProgressBar
    private val progressTextOverlay: ProgressTextOverlay
    private var roundedCornersClipPath: Path = Path() // This path is used to clip the progress background and drawable to the desired corner radius

    init {
        isSaveEnabled = true
        setWillNotDraw(false) // Allows this custom view to override onDraw()

        val view = LayoutInflater.from(context).inflate(R.layout.layout_rounded_progress_bar, this, false)
        progressBar = view.rounded_progress_bar
        progressTextOverlay = view.progress_text_overlay
        progressBar.max = PROGRESS_BAR_MAX * PROGRESS_SCALAR // This is done so animations look smoother

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
        val newProgressBgColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbBackgroundColor, defaultProgressBgColor)
        if (newProgressBgColor != defaultProgressBgColor) setProgressBgColor(newProgressBgColor)

        // Set text size from xml attributes (If exists and isn't the default value)
        val newTextSize = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbTextSize, defaultTextSize)
        if (newTextSize != defaultTextSize) setTextSize(newTextSize)

        // Set progress bar text color via xml (If exists and isn't the default value)
        val newTextColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbProgressTextColor, defaultTextColorRes)
        if (newTextColor != defaultTextColorRes) setTextColor(newTextColor)

        // Set background text color via xml (If exists and isn't the default value)
        val newBgTextColor = rpbAttributes.getResourceId(R.styleable.RoundedProgressBar_rpbBackgroundTextColor, defaultBgTextColorRes)
        if (newBgTextColor != defaultBgTextColorRes) setBgTextColor(newBgTextColor)

        // Show or hide progress text via xml (If exists and isn't the default value)
        val newShowProgressText = rpbAttributes.getBoolean(R.styleable.RoundedProgressBar_rpbShowProgressText, defaultShowProgressText)
        if (newShowProgressText != defaultShowProgressText) showProgressText(newShowProgressText)

        // Set animation length via xml (If exists and isn't the default value)
        val newAnimationLength = rpbAttributes.getInteger(R.styleable.RoundedProgressBar_rpbAnimationLength, defaultAnimationLength)
        if (newAnimationLength != defaultAnimationLength) setAnimationLength(newAnimationLength.toLong())

        // Set whether the rounded corner radius can spill into other rounded corner areas
        val newIsRadiusRestricted = rpbAttributes.getBoolean(R.styleable.RoundedProgressBar_rpbIsRadiusRestricted, defaultIsRadiusRestricted)
        if (newIsRadiusRestricted != defaultIsRadiusRestricted) setIsRadiusRestricted(newIsRadiusRestricted)

        // Set the side padding for the progress indicator text
        val newTextPadding = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbTextPadding, defaultTextPadding)
        if (newTextPadding != defaultTextPadding) setTextPadding(newTextPadding)

        // Set corner radius via xml (If exists and isn't the default value)
        getCornerRadiusFromAttrs(rpbAttributes)

        rpbAttributes.recycle()
    }

    /**
     * This function gets all the requested corner radius info and ensures the view is updated only
     * once for efficiency sake.
     */
    private fun getCornerRadiusFromAttrs(rpbAttributes: TypedArray) {
        var resultingCornerRadiusTL = defaultCornerRadius
        var resultingCornerRadiusTR = defaultCornerRadius
        var resultingCornerRadiusBR = defaultCornerRadius
        var resultingCornerRadiusBL = defaultCornerRadius

        val newBlanketCornerRadius = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbCornerRadius, NO_CORNER_RADIUS_ATTR_SET)
        if (newBlanketCornerRadius != NO_CORNER_RADIUS_ATTR_SET) {
            resultingCornerRadiusTL = newBlanketCornerRadius
            resultingCornerRadiusTR = newBlanketCornerRadius
            resultingCornerRadiusBR = newBlanketCornerRadius
            resultingCornerRadiusBL = newBlanketCornerRadius
        }

        val newCornerRadiusTL = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbCornerRadiusTopLeft, NO_CORNER_RADIUS_ATTR_SET)
        if (newCornerRadiusTL != NO_CORNER_RADIUS_ATTR_SET) resultingCornerRadiusTL = newCornerRadiusTL
        val newCornerRadiusTR = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbCornerRadiusTopRight, NO_CORNER_RADIUS_ATTR_SET)
        if (newCornerRadiusTR != NO_CORNER_RADIUS_ATTR_SET) resultingCornerRadiusTR = newCornerRadiusTR
        val newCornerRadiusBR = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbCornerRadiusBottomRight, NO_CORNER_RADIUS_ATTR_SET)
        if (newCornerRadiusBR != NO_CORNER_RADIUS_ATTR_SET) resultingCornerRadiusBR = newCornerRadiusBR
        val newCornerRadiusBL = rpbAttributes.getDimension(R.styleable.RoundedProgressBar_rpbCornerRadiusBottomLeft, NO_CORNER_RADIUS_ATTR_SET)
        if (newCornerRadiusBL != NO_CORNER_RADIUS_ATTR_SET) resultingCornerRadiusBL = newCornerRadiusBL

        setCornerRadius(
            resultingCornerRadiusTL,
            resultingCornerRadiusTR,
            resultingCornerRadiusBR,
            resultingCornerRadiusBL
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lastReportedHeight = h
        lastReportedWidth = w
        redrawCorners()
    }

    private fun redrawCorners() {
        setCornerRadius(cornerRadiusTL, cornerRadiusTR, cornerRadiusBR, cornerRadiusBL)
    }

    /**
     * Clips the progress bar view to the desired corner radius. This gives the background drawable
     * a rounded corner and ensures the progress drawable doesn't exceed the outline of the
     * progressbar view when at low values
     */
    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(roundedCornersClipPath)
    }

    /**
     * Recalculates the clip path for the background of this view. When onDraw() is called it will
     * then draw the view with the newly modified clip path.
     */
    private fun updateCanvasClipBounds() {
        val height = lastReportedHeight
        val width = lastReportedWidth

        val radiusTL = calculateAppropriateCornerRadius(cornerRadiusTL, height, isRadiusRestricted)
        val radiusTR = calculateAppropriateCornerRadius(cornerRadiusTR, height, isRadiusRestricted)
        val radiusBR = calculateAppropriateCornerRadius(cornerRadiusBR, height, isRadiusRestricted)
        val radiusBL = calculateAppropriateCornerRadius(cornerRadiusBL, height, isRadiusRestricted)

        roundedCornersClipPath.reset()
        val cornerRadiusList = floatArrayOf(
            radiusTL, radiusTL,
            radiusTR, radiusTR,
            radiusBR, radiusBR,
            radiusBL, radiusBL
        )
        roundedCornersClipPath.addRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadiusList,
            Path.Direction.CW
        )
        invalidate() // Invalidate the layout to draw the new roundedCornersClipPath
    }

    /**
     * Creates the drawable that represents the "background" portion of the progress bar (The thing
     * that shows up behind the progress portion)
     */
    private fun createRoundedBackgroundDrawable(): Drawable {
        val newBgDrawable = ShapeDrawable(RectShape())
        newBgDrawable.setColorFilterCompat(context, progressBgColorRes)
        return newBgDrawable
    }

    /**
     * Creates the drawable that represents the "completed" portion of the progress bar
     */
    private fun createRoundedProgressDrawable(): Drawable {
        val topLeftRadius = calculateAppropriateCornerRadius(cornerRadiusTL, lastReportedHeight, isRadiusRestricted)
        val topRightRadius = calculateAppropriateCornerRadius(cornerRadiusTR, lastReportedHeight, isRadiusRestricted)
        val bottomRightRadius = calculateAppropriateCornerRadius(cornerRadiusBR, lastReportedHeight, isRadiusRestricted)
        val bottomLeftRadius = calculateAppropriateCornerRadius(cornerRadiusBL, lastReportedHeight, isRadiusRestricted)

        val cornerRadiusValues = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius
        )

        val roundedDrawable = ShapeDrawable(RoundRectShape(cornerRadiusValues, null, null))
        roundedDrawable.setColorFilterCompat(context, progressColorRes)
        return ScaleDrawable(roundedDrawable, Gravity.START, 1f, -1f)
    }

    /**
     * Gets the current progress level. This is the progress level used internally by the
     * ProgressBar class. This method is ONLY used to re-initialize the size of the ScaleDrawable
     * drawable in the event a new progress drawable is set (like when changing the corner radius)
     * @see ScaleDrawable.setLevel()
     */
    private fun calculateScaleDrawableLevel(curPercentage: Double): Int {
        return (curPercentage * SCALE_DRAWABLE_MULTIPLIER).roundToInt()
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
    // ######### PUBLIC METHODS ######### //
    // ################################## //

    /**
     * @param[progressPercentage] is a value between 0 and 100 inclusive representing the percent
     * completion of the progress bar. Any values outside this range will be normalized to be inside
     * the range
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
        val layerToModify = (progressBar.progressDrawable as LayerDrawable)
            .getDrawable(PROG_DRAWABLE_LAYER_INDEX)
        layerToModify.setColorFilterCompat(context, colorRes)
    }

    fun setProgressBgColor(colorRes: Int) {
        progressBgColorRes = colorRes
        val layerToModify = (progressBar.progressDrawable as LayerDrawable)
            .getDrawable(PROG_BACKGROUND_LAYER_INDEX)
        layerToModify.setColorFilterCompat(context, colorRes)
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
        showProgressText = shouldShowProgressText
        progressTextOverlay.showProgressText(shouldShowProgressText)
    }

    fun setAnimationLength(newAnimationLength: Long) {
        animationLength = newAnimationLength
    }

    /**
     * Sets the corner radius for all corners the progress bar (includes progress background and
     * progress drawable)
     * @param radiusInDp must be in units of dp
     */
    fun setCornerRadius(radiusInDp: Float) {
        setCornerRadius(radiusInDp, radiusInDp, radiusInDp, radiusInDp)
    }

    /**
     * Sets the corner radius for each corner of the progress bar (includes progress background and
     * progress drawable)
     * @param radiusInDp must be in units of dp
     */
    fun setCornerRadius(
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomRightRadius: Float,
        bottomLeftRadius: Float
    ) {
        cornerRadiusTL = topLeftRadius
        cornerRadiusTR = topRightRadius
        cornerRadiusBR = bottomRightRadius
        cornerRadiusBL = bottomLeftRadius
        updateCanvasClipBounds()

        val newProgressDrawable = LayerDrawable(
            arrayOf(
                createRoundedBackgroundDrawable(),
                createRoundedProgressDrawable()
            )
        )
        progressBar.progressDrawable = newProgressDrawable

        // After modifying the progress drawables we need to set the initial value of the
        //  progress drawable completion level
        val currentProgressDrawable = (progressBar.progressDrawable as LayerDrawable)
            .getDrawable(PROG_DRAWABLE_LAYER_INDEX)
        currentProgressDrawable.level = calculateScaleDrawableLevel(getProgressPercentage())
    }

    /**
     * Allows corners to be curved past their "area/corner" of the progress bar.
     *
     * By default corners can only be curved 90 degrees and won't curve into the "area" of
     * a different corner.
     */
    fun setIsRadiusRestricted(isRestricted: Boolean) {
        isRadiusRestricted = isRestricted
        redrawCorners()
    }

    /**
     * Sets the paddingStart (aka paddingLeft) and paddingEnd (aka paddingRight) of the progress
     * completion text.
     */
    fun setTextPadding(newTextPadding: Float) {
        textPadding = newTextPadding
        progressTextOverlay.setTextPadding(newTextPadding)
    }


    // ################################### //
    // ### SAVE STATE BOILERPLATE CODE ### //
    // ################################### //

    public override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.savedCurProgress = curProgress
        savedState.savedPrevTextPositionRatio = prevTextPositionRatio
        savedState.savedProgressColorRes = progressColorRes
        savedState.savedProgressBgColorRes = progressBgColorRes
        savedState.savedAnimationLength = animationLength
        savedState.savedCornerRadiusTL = cornerRadiusTL
        savedState.savedCornerRadiusTR = cornerRadiusTR
        savedState.savedCornerRadiusBR = cornerRadiusBR
        savedState.savedCornerRadiusBL = cornerRadiusBL
        savedState.savedIsRadiusRestricted = isRadiusRestricted

        savedState.savedTextSize = textSize
        savedState.savedTextColorRes = textColorRes
        savedState.savedBgTextColorRes = bgTextColorRes
        savedState.savedShowProgressText = showProgressText
        savedState.savedTextPadding = textPadding
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
            cornerRadiusTL = state.savedCornerRadiusTL
            cornerRadiusTR = state.savedCornerRadiusTR
            cornerRadiusBR = state.savedCornerRadiusBR
            cornerRadiusBL = state.savedCornerRadiusBL
            isRadiusRestricted = state.savedIsRadiusRestricted
            setCornerRadius(cornerRadiusTL, cornerRadiusTR, cornerRadiusBR, cornerRadiusBL)
            setProgressBgColor(progressBgColorRes)
            setProgressColor(progressColorRes)
            setProgressPercentage(curProgress, false)
            // ProgressTextOverlay related
            textSize = state.savedTextSize
            textColorRes = state.savedTextColorRes
            bgTextColorRes = state.savedBgTextColorRes
            showProgressText = state.savedShowProgressText
            textPadding = state.savedTextPadding
            setTextSize(textSize)
            setTextColor(textColorRes)
            setBgTextColor(bgTextColorRes)
            showProgressText(showProgressText)
            setTextPadding(textPadding)
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

    internal class SavedState : BaseSavedState {
        // RoundedProgressBar related
        var savedCurProgress: Double = 0.0
        var savedPrevTextPositionRatio: Float = 0f
        var savedProgressColorRes: Int = 0
        var savedProgressBgColorRes: Int = 0
        var savedAnimationLength: Long = 0L
        var savedCornerRadiusTL: Float = 0f
        var savedCornerRadiusTR: Float = 0f
        var savedCornerRadiusBR: Float = 0f
        var savedCornerRadiusBL: Float = 0f
        var savedIsRadiusRestricted: Boolean = true

        // ProgressTextOverlay related
        var savedTextSize: Float = 0f
        var savedTextColorRes: Int = 0
        var savedBgTextColorRes: Int = 0
        var savedShowProgressText: Boolean = true
        var savedTextPadding: Float = 0f

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel) : super(source) {
            savedCurProgress = source.readDouble()
            savedPrevTextPositionRatio = source.readFloat()
            savedProgressColorRes = source.readInt()
            savedProgressBgColorRes = source.readInt()
            savedAnimationLength = source.readLong()
            savedCornerRadiusTL = source.readFloat()
            savedCornerRadiusTR = source.readFloat()
            savedCornerRadiusBR = source.readFloat()
            savedCornerRadiusBL = source.readFloat()
            savedIsRadiusRestricted = source.readByte() != 0.toByte()

            savedTextSize = source.readFloat()
            savedTextColorRes = source.readInt()
            savedBgTextColorRes = source.readInt()
            savedShowProgressText = source.readByte() != 0.toByte()
            savedTextPadding = source.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeDouble(savedCurProgress)
            out.writeFloat(savedPrevTextPositionRatio)
            out.writeInt(savedProgressColorRes)
            out.writeInt(savedProgressBgColorRes)
            out.writeLong(savedAnimationLength)
            out.writeFloat(savedCornerRadiusTL)
            out.writeFloat(savedCornerRadiusTR)
            out.writeFloat(savedCornerRadiusBR)
            out.writeFloat(savedCornerRadiusBL)
            out.writeByte(if(savedIsRadiusRestricted) 1.toByte() else 0.toByte())

            out.writeFloat(savedTextSize)
            out.writeInt(savedTextColorRes)
            out.writeInt(savedBgTextColorRes)
            out.writeByte(if (savedShowProgressText) 1.toByte() else 0.toByte())
            out.writeFloat(savedTextPadding)
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
}