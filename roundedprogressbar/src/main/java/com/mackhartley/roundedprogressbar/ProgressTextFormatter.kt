package com.mackhartley.roundedprogressbar

interface ProgressTextFormatter {

    /**
     * @param [progressValue] - Current value of the progress bar (for example, 25% would be 0.25)
     * @return Text that should be displayed on the progress bar
     */
    fun getProgressText(progressValue: Float): String

    /**
     * This value sets the minimum width for the progress text view. It is primarily used for
     * cosmetic reasons in cases where value N+1 is a longer string than value N
     * (for example "10%" vs "9%"). Setting a min width string can avoid having the progress text
     * switch sides as frequently. An example of its use can be found in [DefaultProgressTextFormatter]
     */
    val minWidthString: String
        get() = ""
}