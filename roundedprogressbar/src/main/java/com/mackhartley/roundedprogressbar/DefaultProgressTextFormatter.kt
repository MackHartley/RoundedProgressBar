package com.mackhartley.roundedprogressbar

import com.mackhartley.roundedprogressbar.utils.getPercentageString

/**
 * Default implementation of the [ProgressTextFormatter]. The behavior and options available here
 * should suffice for most use cases.
 *
 * @param onlyShowTrue0 If this is true then 0% will only ever be shown if the [completionRatio]
 * is actually 0. This means values like 0.00001 will be shown as 1%. Values of 0.0 will be shown
 * as 0%.
 * @param onlyShowTrue100 If this is true then 100% will only ever be shown if the [completionRatio]
 * is actually 100. This means values like 99.9999 will be shown as 99%. Values of 100.0 will be
 * shown as 100%.
 *
 * The params [onlyShowTrue0] and [onlyShowTrue100] might seem unnecessary, but they can be
 * important depending on your use case. For example, consider a scenario where your app rewards
 * a user if they complete 1000 tasks. If the user has completed 995 tasks, their completion ratio
 * would be 0.995 or 99.5%. Since this class only shows [Integer] values, that 99.5% would be
 * rounded up to 100% and then displayed to the user. That might mislead your user into thinking
 * they are complete with the challenge even though 5 tasks remain. That would be a bad user
 * experience, and in this case you would want [onlyShowTrue100] to be set to true so the user only
 * sees 100% when they have completed 1000/1000 tasks.
 */
class DefaultProgressTextFormatter(
    private val onlyShowTrue0: Boolean = false,
    private val onlyShowTrue100: Boolean = false
) : ProgressTextFormatter {

    companion object {
        // Used to calculate the min width of text view. This is done for UX reasons, to
        //   minimize how often the display number swaps between the inside and external position.
        //   Without this minimum, 8% could be outside the bar, 9% on inside (since the bar grew)
        //   and 10% on outside. All that swapping looks less smooth to the user
        const val MIN_WIDTH_STRING = "10%"
    }

    override val minWidthString: String
        get() = MIN_WIDTH_STRING

    override fun getProgressText(progressValue: Float): String {
        return getPercentageString(progressValue, onlyShowTrue0, onlyShowTrue100)
    }
}